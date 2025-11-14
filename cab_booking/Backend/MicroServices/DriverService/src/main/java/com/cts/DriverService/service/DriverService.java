package com.cts.DriverService.service;



import com.cts.DriverService.dto.DriverRegistration;
import com.cts.DriverService.entity.Driver;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DriverService {
    Driver saveDriver(DriverRegistration driverRegistration);

    List<Driver> getAllDriver();

    Driver getDriverById(int id);

    Driver deleteDriverById(int id);

//    void updateDriver(Driver driver);

    Driver getDriverByPhone(long phone);

    Driver updateDriver(String token, DriverRegistration driverRegistration);
    ResponseEntity<?> getDriverDetails(String authHeader);
}
