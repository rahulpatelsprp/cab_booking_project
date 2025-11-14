package com.cts.DriverService.service.impl;


import com.cts.DriverService.controller.UserController;
import com.cts.DriverService.dto.DriverRegistration;
import com.cts.DriverService.dto.DriverResponse;
import com.cts.DriverService.dto.Role;
import com.cts.DriverService.dto.UserDTO;
import com.cts.DriverService.entity.Driver;
import com.cts.DriverService.exception.UserDetailsNotSavedException;
import com.cts.DriverService.exception.UserNotFoundException;
import com.cts.DriverService.repo.DriverRepo;

import com.cts.DriverService.service.DriverService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class DriverServiceImpl implements DriverService {

    private final DriverRepo driverRepo;


    private final UserController userController;



    public DriverServiceImpl(DriverRepo driverRepo, UserController userController) {
        this.driverRepo = driverRepo;
        this.userController = userController;

    }
    @Transactional(rollbackOn =  Exception.class)
    @Override
    public Driver saveDriver(DriverRegistration driverRegistration) {
         Driver driver = new Driver();
        driver.setName(driverRegistration.getName());
        driver.setLicenseNumber(driverRegistration.getLicenseNumber());
        driver.setVehicleDetails(driverRegistration.getVehicleDetails());
        driver.setPhone(driverRegistration.getPhone());

       UserDTO user = new UserDTO();
       user.setPhone(driverRegistration.getPhone());
       user.setName(driverRegistration.getName());
       user.setPasswordHash(driverRegistration.getPasswordHash());
       user.setRole(Role.DRIVER);
       user.setEmail(driverRegistration.getEmail());

           UserDTO savedUser = userController.saveUser(user).getBody(); // userService.saveUser(user); call post method for saving the user
           if(savedUser == null){
             throw new UserDetailsNotSavedException("User is not saved");
           }
           driver.setDriverId(savedUser.getUserId());

       return driverRepo.save(driver);
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
    @Transactional(rollbackOn =  Exception.class)
    @Override
    public Driver updateDriver(String authHeader, DriverRegistration driverRegistration) {
        Driver driver = new Driver();
        driver.setName(driverRegistration.getName());
        driver.setPhone(driverRegistration.getPhone());
        driver.setVehicleDetails(driverRegistration.getVehicleDetails());
        driver.setLicenseNumber(driverRegistration.getLicenseNumber());
        UserDTO user=new UserDTO();
        user.setPasswordHash(driverRegistration.getPasswordHash());
       user.setEmail(driverRegistration.getEmail());
       user.setName(driverRegistration.getName());
       user.setRole(Role.DRIVER);
       user.setPhone(driverRegistration.getPhone());
           UserDTO userDTO= userController.updateDetails(authHeader,user).orElseThrow(()->new UserNotFoundException("User is not found "));
            if (userDTO == null){
                throw new UserNotFoundException("User not found with ID: " + authHeader);
            }
           driver.setDriverId(userDTO.getUserId());
        return driverRepo.save(driver);
    }
    @Override
    public ResponseEntity<?> getDriverDetails(String authHeader) {
        UserDTO user =
                userController.getUserByToken(authHeader).orElseThrow(() -> new UserNotFoundException("User is not found  :" + authHeader));
        Driver driver = driverRepo.findByPhone(user.getPhone());
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
