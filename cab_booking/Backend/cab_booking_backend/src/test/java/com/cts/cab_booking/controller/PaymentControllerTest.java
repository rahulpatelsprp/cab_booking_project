package com.cts.cab_booking.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.Payment;
import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.helper.Method;
import com.cts.cab_booking.helper.Status;
import com.cts.cab_booking.service.PaymentService;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    private Payment samplePayment;
    private Ride sampleRide;
    private Driver sampleDriver;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        sampleUser = User.builder()
                .userId(1)
                .name("Rahul")
                .email("rahul@example.com")
                .build();

        Driver.builder()
                .driverId(1)
                .name("Driver Kumar")
                .build();

        sampleRide = Ride.builder()
                .riderId(1)
                .userRide(sampleUser)
                .driver(sampleDriver)
                .build();

        samplePayment = Payment.builder()
                .paymentId(1)
                .ride(sampleRide)
                .userPayment(sampleUser)
                .amount(500)
                .method(Method.UPI)
                .status(Status.PENDING)
                .time(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldReturnPaymentByRideId() throws Exception {
        when(paymentService.getPaymentByRideId(1)).thenReturn(samplePayment);

        mockMvc.perform(get("/api/payments/byrideid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    void shouldReturnPaymentByPaymentId() throws Exception {
        when(paymentService.getPaymentById(1)).thenReturn(samplePayment);

        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.method").value("UPI"));
    }



  
    @Test
    void shouldReturnRideByPaymentId() throws Exception {
        when(paymentService.getRideByPaymentId(1)).thenReturn(sampleRide);

        mockMvc.perform(get("/api/payments/getRide/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riderId").value(1));
    }
}