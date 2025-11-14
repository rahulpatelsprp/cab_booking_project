package com.cts.cab_booking.controller;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.helper.RideRigester;
import com.cts.cab_booking.helper.Status;
import com.cts.cab_booking.helper.UserRide;
import com.cts.cab_booking.service.RideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RideController {

    private final RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }


    @GetMapping
    public List<Ride> getAllRides() {

        List<Ride> rides = rideService.getAllRide();
        return rides;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestHeader("Authorization") String authHeader, @RequestBody RideRigester ride) {

        String token = authHeader.replace("Bearer ", "");

        return ResponseEntity.ok(rideService.saveRide(token, ride));
    }

    @GetMapping("/user")
    public List<Ride> getAllRideByUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return rideService.getAllRideByUserId(token);
    }

    @GetMapping("/driver")
    public List<Ride> getAllRideByDriverId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return rideService.getAllRideByDriverId(token);
    }


    @GetMapping("/driveranduser")
    public List<Ride> getRideByDriverIdAndUserId(@RequestBody UserRide ride) {
        return rideService.getRideByDriverAndUser(ride.getDriverId(), ride.getUserId());
    }

    @GetMapping("/userByRiderId/{riderId}")
    public User getUser(@PathVariable int riderId) {
        Ride ride = rideService.getRideById(riderId);
        log.info("Fetching user by ride id" + ride.getUserRide());
        return ride.getUserRide();
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptRideRequest(@RequestHeader("Authorization") String authHeader, @RequestBody Ride ride) {
        try {
            log.info("Pickup Location: {}", ride.getPickUpLocation());
            String token = authHeader.replace("Bearer ", "");

            Ride updatedRide = rideService.updateRide(token, ride);
            log.info(updatedRide.getRiderId() + "===============================");
            return ResponseEntity.ok(updatedRide);
        } catch (Exception e) {
            log.error("Error accepting ride request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the ride request.");
        }
    }

    @GetMapping("/{rideId}")
    public Ride getRideStatusById(@PathVariable("rideId") int rideId) {
        Ride ride = rideService.getRideById(rideId);
        return ride;
    }

    @GetMapping("/driverByRiderId/{riderId}")
    public Driver getDriverByRide(@PathVariable int riderId) {
        Ride ride = rideService.getRideById(riderId);
        return ride.getDriver();
    }

    @GetMapping("/byStatus/{status}")
    public List<Ride> getRideByStatus(@PathVariable Status status) {

        return rideService.getAllRideBYStatus(status);
    }

    @PutMapping("/byStatus/{status}")
    public Ride getRideByStatus(@PathVariable Status status, @RequestBody Ride ride) {
        return rideService.changeRideStatusByRiderId(status, ride.getRiderId());
    }

    @GetMapping("/pending")
    public Ride getPendingRideOfCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return rideService.getPendingRideOfCurrentUser(token);
    }
}

