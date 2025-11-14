package com.cts.RideService.controller;

import com.cts.RideService.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name="USERSERVICE")
public interface UserController {


    @GetMapping("/api/users/by_token")
    Optional<UserDTO> getUserByToken(@RequestHeader("Authorization") String token);

    @GetMapping("/api/users/profile/{id}")
    Optional<UserDTO> getUserDetailsByUserId(@PathVariable("id") int id);

}