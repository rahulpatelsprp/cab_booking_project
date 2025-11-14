package com.cts.RideService.controller;

import com.cts.RideService.dto.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENTSERVICE")
public interface PaymentController {
    @PostMapping("/api/payments")
     ResponseEntity<PaymentDTO> savePayment(@RequestBody PaymentDTO payment);
}
