package com.cts.RatingService.controller;

import com.cts.RatingService.dto.UserIdAndDriverIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "RIDESERVICE")
public interface RideController {
    @GetMapping("/api/rides/ride_details/{id}")
    public UserIdAndDriverIdDto  getRideDetails(@PathVariable("id") int rideId) ;

}
