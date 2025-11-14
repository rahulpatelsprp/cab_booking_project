package com.cts.cab_booking.controller;

import com.cts.cab_booking.entity.Payment;
import com.cts.cab_booking.helper.PaymentResponse;
import com.cts.cab_booking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {


    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/byrideid/{rideId}")
    public ResponseEntity<Payment> getPaymentByRideId(@PathVariable int rideId) {
        Payment payment = paymentService.getPaymentByRideId(rideId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentByPaymentId(@PathVariable int paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/getPaymentDetails/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByPaymentId(@PathVariable int paymentId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.getPaymentDetailsByPaymentId(paymentId));
    }

    @PutMapping
    public ResponseEntity<Payment> changePaymentStatusAndMethod(@RequestBody Payment payment) {
        Payment updatedPayment = paymentService.updatePayment(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/getRide/{paymentId}")
    public ResponseEntity<?> getRideByPaymentId(@PathVariable int paymentId) {
        return ResponseEntity.ok(paymentService.getRideByPaymentId(paymentId));
    }
}
