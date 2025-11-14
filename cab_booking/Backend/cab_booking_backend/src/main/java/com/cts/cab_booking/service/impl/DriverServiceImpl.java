package com.cts.cab_booking.service.impl;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.exception.UserNotFoundException;
import com.cts.cab_booking.helper.DriverRegistration;
import com.cts.cab_booking.helper.DriverResponse;
import com.cts.cab_booking.repository.DriverRepo;
import com.cts.cab_booking.repository.UserRepo;
import com.cts.cab_booking.securityConfig.JwtUtil;
import com.cts.cab_booking.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class DriverServiceImpl implements DriverService {

    private final DriverRepo driverRepo;

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    public DriverServiceImpl(DriverRepo driverRepo, UserRepo userRepo, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.driverRepo = driverRepo;
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveDriver(Driver driver) {
        log.info("Saving driver with phone: {}", driver.getPhone());
        driverRepo.save(driver);
    }

    @Override
    public List<Driver> getAllDriver() {
        log.info("Fetching all drivers from the database.");
        return driverRepo.findAll();
    }

    @Override
    public Driver getDriverById(int id) {
        log.info("Fetching driver by ID: {}", id);
        return driverRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Driver not found with ID: {}", id);
                    return new UserNotFoundException("Driver is not found id : " + id);
                });
    }

    @Override
    public Driver deleteDriverById(int id) {
        log.info("Attempting to delete driver with ID: {}", id);
        Optional<Driver> driver = driverRepo.findById(id);
        if (driver.isPresent()) {
            driverRepo.delete(driver.get());
            log.info("Driver deleted successfully with ID: {}", id);
            return driver.get();
        } else {
            log.warn("Driver not found for deletion with ID: {}", id);
            return null;
        }
    }


    @Override
    public Driver getDriverByPhone(long phone) {
        log.info("Fetching driver by phone number: {}", phone);
        return driverRepo.findByPhone(phone);
    }

    @Override
    public Driver updateDriver(String token, DriverRegistration driverRegistration) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepo.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User is not found for this username"));
        Driver driver = new Driver();

        driver.setName(driverRegistration.getName());
        driver.setPhone(driverRegistration.getPhone());
        driver.setVehicleDetails(driverRegistration.getVehicleDetails());
        driver.setLicenseNumber(driverRegistration.getLicenseNumber());
        user.setPasswordHash(passwordEncoder.encode(driverRegistration.getPasswordHash()));
        user.setEmail(driverRegistration.getEmail());
        user.setName(driverRegistration.getName());
        User savedUser = userRepo.save(user);

        driver.setDriverId(user.getUserId());
        return driverRepo.save(driver);
    }

    @Override
    public ResponseEntity<?> getDriverDetails(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepo.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User is not found  :" + username));
        Driver driver = driverRepo.findById(user.getUserId()).orElseThrow(() -> new UserNotFoundException("Driver is not found id :" + user.getUserId()));
        DriverResponse driverResponse = DriverResponse.builder()
                .email(user.getEmail())
                .name(driver.getName()).
                phone(driver.getPhone())
                .licenseNumber(driver.getLicenseNumber())
                .vehicleDetails(driver.getVehicleDetails())
                .driverSinceYear(2025)
                .build();

        return ResponseEntity.ok(driverResponse);
    }
}
