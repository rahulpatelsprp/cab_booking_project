package com.cts.cab_booking.service;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.helper.DriverRegistration;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DriverService {
    void saveDriver(Driver driver);

    List<Driver> getAllDriver();

    Driver getDriverById(int id);

    Driver deleteDriverById(int id);

//    void updateDriver(Driver driver);

    Driver getDriverByPhone(long phone);

    Driver updateDriver(String token, DriverRegistration driverRegistration);

    ResponseEntity<?> getDriverDetails(String token);
}
