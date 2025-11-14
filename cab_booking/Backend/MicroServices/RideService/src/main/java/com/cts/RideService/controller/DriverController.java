package com.cts.RideService.controller;

import com.cts.RideService.dto.DriverDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name="DRIVERSERVICE")
public interface DriverController {
    @GetMapping("/api/drivers/{id}")
    ResponseEntity<DriverDTO> getDriverByRiderId(@PathVariable("id") int driverId);
}
