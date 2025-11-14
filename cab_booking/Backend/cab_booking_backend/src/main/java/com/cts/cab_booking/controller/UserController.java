package com.cts.cab_booking.controller;

import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.exception.UserAlreadyFoundException;
import com.cts.cab_booking.helper.AuthRequest;
import com.cts.cab_booking.helper.AuthResponse;
import com.cts.cab_booking.helper.Role;
import com.cts.cab_booking.securityConfig.JwtUtil;
import com.cts.cab_booking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Users Controller", description = "User Crud operation and login page")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "User Registration")
    @PostMapping("/register")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        try {
            Optional<User> isUser = userService.getUserByEmail(user.getEmail());
            if (isUser.isPresent()) {
                throw new UserAlreadyFoundException("User is already found");
            }
            user.setRole(Role.USER);
            userService.saveUser(user);
        } catch (UserAlreadyFoundException e) {
            log.error("User is not saved ====" + e.getMessage());
            return ResponseEntity.ok("AlreadyFound");
        }
        return ResponseEntity.ok("SavedUser");
    }

    @Operation(summary = "Get User by id")
    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        log.info("==================" + user.getEmail());
        return ResponseEntity.ok(user);
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

    @Operation(summary = "Get current user with token")
    @GetMapping("/current")
    public User getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return userService.getUserByToken(token);
    }

    @Operation(summary = "Update user details")
    @PutMapping
    public ResponseEntity<?> updateDetails(@RequestHeader("Authorization") String authHeader, @RequestBody User userData) {
        log.info("Updated user details");
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.updateUser(token, userData));
    }


}


// @Operation(summary = "User login authentication")
//   @PostMapping("/login")
//   public ResponseEntity<?> loginUser(@RequestBody User user) {
//        User userDetails = userService.getUserByEmail(user.getEmail()).get();
//
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        // Use BCrypt to compare hashed passwords
//        if (user .getPasswordHash().equals( userDetails.getPasswordHash())) {
//            log.info("Login successful for user: {}", user.getEmail());
//
//            // Return only non-sensitive data
//            User safeUser = new User();
//            safeUser.setUserId(userDetails.getUserId());
//            safeUser.setEmail(userDetails.getEmail());
//            safeUser.setName(userDetails.getName());
//            safeUser.setPhone(userDetails.getPhone());
//            log.info("Details ============== "+userDetails.getRole());
//            safeUser.setRole(userDetails.getRole());
//            return ResponseEntity.ok(safeUser);
//    }
//
//    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//}