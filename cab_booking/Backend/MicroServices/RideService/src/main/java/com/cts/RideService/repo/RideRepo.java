package com.cts.RideService.repo;

import com.cts.RideService.dto.Status;
import com.cts.RideService.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepo extends JpaRepository<Ride, Integer> {


    List<Ride> findByUserIdOrderByRiderIdDesc(int userId);

    List<Ride> findByStatus(Status status);

    List<Ride> findByDriverIdOrderByRiderIdDesc(Integer driverId);

    List<Ride> findByDriverIdAndUserId(Integer driverId, Integer userId);



    Ride findByStatusAndUserId(Status status, int userId);
}
