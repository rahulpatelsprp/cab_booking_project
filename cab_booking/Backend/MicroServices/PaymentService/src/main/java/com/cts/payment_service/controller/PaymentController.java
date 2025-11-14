package com.cts.payment_service.controller;


import com.cts.payment_service.dto.PaymentResponse;
import com.cts.payment_service.service.PaymentService;
import com.cts.payment_service.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/byrideid/{rideId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','DRIVER')")
    public ResponseEntity<Payment> getPaymentByRideId(@PathVariable int rideId) {
        return ResponseEntity.ok(paymentService.getPaymentByRideId(rideId));
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','DRIVER')")
    public ResponseEntity<Payment> getPaymentByPaymentId(@PathVariable int paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/getPaymentDetails/{paymentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByPaymentId(@PathVariable int paymentId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.getPaymentDetailsByPaymentId(paymentId));
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Payment> changePaymentStatusAndMethod(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.updatePayment(payment));
    }

    @GetMapping("/getRide/{paymentId}")
    @PreAuthorize("hasAnyRole('USER','DRIVER')")
    public ResponseEntity<Integer> getRideIdByPaymentId(@PathVariable int paymentId) {
        return ResponseEntity.ok(paymentService.getRideIdByPaymentId(paymentId).get());
    }

    // Accessible by USER
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Payment> savePayment(@RequestBody Payment payment) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.savePayment(payment));
    }

    // Accessible by ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayment();
    }
}