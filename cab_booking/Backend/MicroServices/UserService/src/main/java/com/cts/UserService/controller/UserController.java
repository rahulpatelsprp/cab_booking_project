package com.cts.UserService.controller;


import com.cts.UserService.dto.Role;
import com.cts.UserService.service.UserService;
import com.cts.UserService.dto.AuthRequest;
import com.cts.UserService.dto.AuthResponse;
import com.cts.UserService.entity.User;
import com.cts.UserService.exception.UserAlreadyFoundException;
import com.cts.UserService.securityConfig.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@Slf4j
@Tag(name = "Users Controller", description = "User Crud operation and login page")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "User Registration")
    @PostMapping("/register")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser=null;
        try {
            log.info("Saving user {}", user);
            Optional<User> isUser = userService.getUserByEmail(user.getEmail());
            if (isUser.isPresent()) {
                throw new UserAlreadyFoundException("User is already found");
            }
    user.setRole(user.getRole()==null? Role.USER:user.getRole());
       savedUser= userService.saveUser(user);
        } catch (UserAlreadyFoundException e) {
            log.error("User is not saved ====" + e.getMessage());
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(savedUser);
    }




    @Operation(summary = "Authenticate user login by username and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        log.info("User login started ============================== {}", request.getUsername());


        User user = userService.getUserByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(new AuthResponse(token, user.getRole()));
    }


    @Operation(summary = "Delete user by id")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User delete(@PathVariable Integer id) {
        User user1 = null;
        try {
            user1 = userService.deleteUserById(id);
            if (user1 == null) {
                throw new Exception("User is not deleted");
            }
        } catch (Exception ex) {
            log.info("User trying to delete but it failed User not found " + ex.getMessage());
        }
        return user1;
    }

    @Operation(summary = "Get all the user details")
    @GetMapping
    public ResponseEntity<?> allUserDetails() {
        log.info("User is getting all user details");
        return ResponseEntity.ok(userService.getAllUser());
    }

    @Operation(summary="Get current user with token")
    @GetMapping("/current")
    public User getCurrentUser(@RequestHeader("Authorization") String authHeader){
        return getUserByToken(authHeader);
    }

    @GetMapping("/by_email")
    public User getUserByEmail(@RequestParam String email) {
        return  userService.getUserByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    @Operation(summary = "Get User by id")
    @GetMapping("/profile/{id}")

    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by_token")
    @PreAuthorize("hasAnyRole('ADMIN','USER','DRIVER')")
    public User getUserByToken(@RequestHeader("Authorization") String authHeader) {
        log.info("User is getting user by token: {}", authHeader);

        String[] parts = authHeader.split(",");
        String bearerToken = null;

        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("Bearer ")) {
                bearerToken = part.substring(7); // Remove "Bearer " prefix
                break;
            }
        }

        if (bearerToken == null) {
            throw new IllegalArgumentException("No valid Bearer token found in Authorization header");
        }

        log.debug("Extracted token: {}", bearerToken);

        return userService.getUserByEmail(
                jwtUtil.extractUsername(bearerToken)
        ).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    @Operation(summary = "Update user details")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER','DRIVER')")
    public ResponseEntity<User> updateDetails(@RequestHeader("Authorization") String authHeader,@RequestBody User userData) {
        log.info("Updated user details");
        String token=authHeader.replace("Bearer ","");
        return ResponseEntity.ok(userService.updateUser(token,userData));
    }

}
