package com.cts.payment_service.controller;

import com.cts.payment_service.dto.RideDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RIDESERVICE")
public interface RideControllerFeign {
    @GetMapping("/api/rides/{rideId}")
    RideDTO getRideStatusById(@PathVariable("rideId") int rideId) ;
}
