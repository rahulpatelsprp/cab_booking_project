package com.cts.cab_booking.controller;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.helper.DriverRegistration;
import com.cts.cab_booking.helper.Role;
import com.cts.cab_booking.service.DriverService;
import com.cts.cab_booking.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class DriverController {

    private final DriverService driverService;

    private final UserService userService;

    @Autowired
    public DriverController(DriverService driverService, UserService userService) {
        this.driverService = driverService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerDriver(@RequestBody DriverRegistration driverRegistration) {
        Driver driver = new Driver();
        driver.setName(driverRegistration.getName());
        driver.setLicenseNumber(driverRegistration.getLicenseNumber());
        driver.setVehicleDetails(driverRegistration.getVehicleDetails());
        driver.setPhone(driverRegistration.getPhone());

        User user = new User();
        user.setPhone(driverRegistration.getPhone());
        user.setName(driverRegistration.getName());
        user.setPasswordHash(driverRegistration.getPasswordHash());
        user.setRole(Role.DRIVER);
        user.setEmail(driverRegistration.getEmail());
        try {
            User savedUser = userService.saveUser(user);
            driver.setUser(user);
            driverService.saveDriver(driver);

        } catch (Exception ex) {
            log.error("Driver is not register " + ex.getMessage());
            return ResponseEntity.status(500).body("Driver registration failed");
        }

        return ResponseEntity.ok("Driver is saved");
    }

    @GetMapping("")
    public ResponseEntity<List<Driver>> allDriverDetails() {
        return ResponseEntity.ok(driverService.getAllDriver());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> driverDetailById(@PathVariable int id) {
        Driver driver = null;
        try {
            driver = driverService.getDriverById(id);
        } catch (Exception e) {
            log.error("Driver is not found");
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok(driver);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletingDriverById(@PathVariable int id) {
        log.warn("Deleting Driver by id " + id);
        return ResponseEntity.ok(driverService.deleteDriverById(id));
    }

    @PutMapping("")
    public ResponseEntity<?> updatingDriverById(@RequestHeader("Authorization") String authHeader, @RequestBody DriverRegistration driverRegistration) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(driverService.updateDriver(token, driverRegistration));
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getDriverDetails(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return driverService.getDriverDetails(token);
    }


}

