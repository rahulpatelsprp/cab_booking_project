package com.cts.cab_booking.service;

import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.helper.RideRigester;
import com.cts.cab_booking.helper.Status;
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

    Ride saveRide(String token, RideRigester ride);

    Ride changeRideStatusByRiderId(Status status, int riderId);

	abstract Ride getPendingRideOfCurrentUser(String token);
}
