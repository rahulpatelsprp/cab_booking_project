package com.cts.RideService.controller;

import com.cts.RideService.dto.Status;
import com.cts.RideService.entity.Ride;
import com.cts.RideService.service.RideService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(RideController.class)
class RideControllerTest {

    @MockBean
    private RideService rideService;
    @Test
    void getAllRideByUserId() {
        Ride ride = Ride.builder()
                .userId(2)
                .driverId(3)
                .riderId(1)
                .status(Status.COMPLETED)
                .dropOffLocation("COIMBATORE")
                .pickUpLocation("IT Park")
                .paymentId(3).build();
        Ride ride2 = Ride.builder()
                .userId(2)
                .driverId(3)
                .riderId(2)
                .status(Status.COMPLETED)
                .dropOffLocation("Chennai")
                .pickUpLocation("IT Park")
                .paymentId(2).build();
        List<Ride> rideList = List.of(new Ride());
    }

    @Test
    void getUser() {
    }

    @Test
    void getRideStatusById() {
    }

    @Test
    void getDriverByRide() {
    }
}