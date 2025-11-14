package com.cts.cab_booking.repository;

import com.cts.cab_booking.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepo extends JpaRepository<Driver, Integer> {
    Driver findByPhone(long phone);
}
