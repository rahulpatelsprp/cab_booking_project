package com.cts.cab_booking.repository;

import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.helper.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RideRepo extends JpaRepository<Ride, Integer> {


    List<Ride> findByUserRideUserIdOrderByRiderIdDesc(int userId);

    List<Ride> findByStatus(Status status);

    List<Ride> findByDriverDriverIdOrderByRiderIdDesc(Integer driverId);

    List<Ride> findByDriverDriverIdAndUserRideUserId(Integer driverId, Integer userId);
    
	Ride getRideByStatusAndUserRideUserId(Status pending, int userId);


}
