package com.cts.DriverService.controller;

import com.cts.DriverService.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name="USERSERVICE")
public interface UserController {
    @PostMapping("/api/users/register")
     ResponseEntity<UserDTO> saveUser(UserDTO user);
    @GetMapping("/api/users/by_email")
    Optional<UserDTO> getUserByEmail(@RequestParam String email);

    @GetMapping("/api/users/by_token")
    Optional<UserDTO> getUserByToken(@RequestHeader("Authorization") String token);
    @PutMapping("/api/users")
    Optional<UserDTO> updateDetails(@RequestHeader("Authorization") String authHeader,@RequestBody UserDTO userData);
}
