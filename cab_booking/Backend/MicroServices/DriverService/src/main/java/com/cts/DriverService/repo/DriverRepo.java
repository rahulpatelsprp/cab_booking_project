package com.cts.DriverService.repo;


import com.cts.DriverService.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepo extends JpaRepository<Driver, Integer> {
    Driver findByPhone(long phone);
}
