package com.cts.DriverService.controller;


import com.cts.DriverService.entity.Driver;
import com.cts.DriverService.dto.DriverRegistration;
import com.cts.DriverService.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/drivers")
public class DriverController {
    @Autowired
    private DriverService driverService;


    @PostMapping("/register")
    public ResponseEntity<?> registerDriver(@RequestBody DriverRegistration driverRegistration) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.saveDriver(driverRegistration));
    }

    @GetMapping("")
    public ResponseEntity<?> allDriverDetails() {

        return ResponseEntity.ok(driverService.getAllDriver());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> driverDetailById(@PathVariable int id) {
        Driver driver = null;
        try {
            driver = driverService.getDriverById(id);
        } catch (Exception e) {
            log.error("Driver is not found");
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(driver);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletingDriverById(@PathVariable int id) {
        log.warn("Deleting Driver by id " + id);
        return ResponseEntity.ok(driverService.deleteDriverById(id));
    }

    @PutMapping("")
    public ResponseEntity<?> updatingDriverById(@RequestHeader("Authorization") String authHeader, @RequestBody DriverRegistration driverRegistration) {
        log.warn("Updating Driver by id " + driverRegistration);
        return ResponseEntity.ok(driverService.updateDriver(authHeader, driverRegistration));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getDriverDetails(@RequestHeader("Authorization") String authHeader) {
        return driverService.getDriverDetails(authHeader);
    }


}

