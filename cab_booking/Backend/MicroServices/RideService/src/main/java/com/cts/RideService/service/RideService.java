package com.cts.RideService.service;


import com.cts.RideService.dto.DriverDTO;
import com.cts.RideService.dto.RideRegister;
import com.cts.RideService.dto.Status;
import com.cts.RideService.dto.UserDTO;
import com.cts.RideService.entity.Ride;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RideService {

    abstract List<Ride> getAllRide();

    abstract Ride getRideById(int id);

    abstract Ride deleteRideById(int id);

    abstract Ride updateRide(String token, Ride ride);

    abstract List<Ride> getAllRideByUserId(String token);

    abstract List<Ride> getAllRideByDriverId(String token);

    abstract List<Ride> getRideByDriverAndUser(int driverId, int userId);

    List<Ride> getAllRideBYStatus(Status status);

    Ride saveRide(String token, RideRegister ride);

    Ride changeRideStatusByRiderId(Status status, int riderId);

    UserDTO getUserByRiderId(int riderId);

    DriverDTO getDriverByRiderId(int riderId);

    Ride getPendingRideOfCurrentUser(String token);
}
