package com.cts.RideService.controller;


import com.cts.RideService.dto.*;
import com.cts.RideService.entity.Ride;
import com.cts.RideService.service.RideService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public List<Ride> getAllRideByUserId(@RequestHeader("Authorization") String authHeader) {
        return rideService.getAllRideByUserId(authHeader);
    }

    @GetMapping("/driver")
    @PreAuthorize("hasRole('DRIVER')")
    public List<Ride> getAllRideByDriverId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return rideService.getAllRideByDriverId(token);
    }

    @GetMapping("/driveranduser")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER')")
    public List<Ride> getRideByDriverIdAndUserId(@RequestBody UserRide ride) {
        return rideService.getRideByDriverAndUser(ride.getDriverId(), ride.getUserId());
    }

    @GetMapping("/userByRiderId/{riderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','DRIVER')")
    public UserDTO getUser(@PathVariable int riderId) {
        log.info("Fetching user by ride id" + riderId);
        return rideService.getUserByRiderId(riderId);
    }

    @PutMapping("/accept")
    @PreAuthorize("hasAnyRole('DRIVER','USER')")
    public ResponseEntity<?> acceptRideRequest(@RequestHeader("Authorization") String authHeader, @RequestBody Ride ride) {
        try {
            Ride updatedRide = rideService.updateRide(authHeader, ride);
            return ResponseEntity.ok(updatedRide);
        } catch (Exception e) {
            log.error("Error accepting ride request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the ride request.");
        }
    }

    @GetMapping("/{rideId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','DRIVER')")
    public Ride getRideStatusById(@PathVariable("rideId") int rideId) {
        log.info("Fetching ride status by rideId" + rideId);
        return rideService.getRideById(rideId);
    }

    @GetMapping("/driverByRiderId/{riderId}")
    @PreAuthorize("hasRole('USER')")
    public DriverDTO getDriverByRide(@PathVariable int riderId) {
        return rideService.getDriverByRiderId(riderId);
    }

    @GetMapping("/byStatus/{status}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','DRIVER')")
    public List<Ride> getRideByStatus(@PathVariable Status status) {
        return rideService.getAllRideBYStatus(status);
    }

    @PutMapping("/byStatus/{status}")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER')")
    public Ride changeRideStatusByRiderId(@PathVariable("status") Status status, @RequestBody Ride ride) {
        return rideService.changeRideStatusByRiderId(status, ride.getRiderId());
    }

    @GetMapping("/ride_details/{rideId}")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER')")
    public UserIdAndDriverIdDto getRideDetails(@PathVariable("rideId") int rideId) {
        Ride ride = rideService.getRideById(rideId);
        return UserIdAndDriverIdDto.builder()
                .userId(ride.getUserId())
                .driverId(ride.getDriverId())
                .build();
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Ride> getAllRides() {
        return rideService.getAllRide();
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> register(@RequestHeader("Authorization") String authHeader, @RequestBody RideRegister ride) {
        return ResponseEntity.ok(rideService.saveRide(authHeader, ride));
    }
    @GetMapping("/pending")
    public Ride getPendingRideOfCurrentUser(@RequestHeader("Authorization") String authHeader) {
        return rideService.getPendingRideOfCurrentUser(authHeader);
    }
}