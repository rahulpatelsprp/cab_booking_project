package com.cts.RatingService.controller;


import com.cts.RatingService.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name="USERSERVICE")
public interface UserController {


    @GetMapping("/api/users/by_token")
    Optional<UserDTO> getUserByToken(@RequestHeader("Authorization") String token);
}
