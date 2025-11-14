package com.cts.cab_booking.service.impl;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.Payment;
import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.helper.PaymentResponse;
import com.cts.cab_booking.helper.Status;
import com.cts.cab_booking.repository.PaymentRepo;
import com.cts.cab_booking.repository.RideRepo;
import com.cts.cab_booking.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final RideRepo rideRepo;

    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepo, RideRepo rideRepo) {
        this.paymentRepo = paymentRepo;
        this.rideRepo = rideRepo;
    }


    @Override
    public Payment savePayment(Payment payment) {
        log.info("Saving new payment for ride ID: {}", payment.getRide().getRiderId());
        payment.setStatus(Status.PENDING);
        return paymentRepo.save(payment);
    }

    @Override
    public List<Payment> getAllPayment() {
        log.info("Fetching all payment records.");
        return paymentRepo.findAll();
    }

    @Override
    public Payment getPaymentById(int id) {
        log.info("Fetching payment by ID: {}", id);
        return paymentRepo.findById(id).orElseThrow(() -> {
            log.warn("Payment not found with ID: {}", id);
            return new RuntimeException("Payment not found with ID: " + id);
        });
    }

    @Override
    public Payment deletePaymentById(int id) {
        log.info("Attempting to delete payment with ID: {}", id);
        Optional<Payment> payment = paymentRepo.findById(id);
        if (payment.isPresent()) {
            paymentRepo.delete(payment.get());
            log.info("Payment deleted successfully with ID: {}", id);
            return payment.get();
        } else {
            log.warn("Payment not found for deletion with ID: {}", id);
            return null;
        }
    }

    @Override
    public Payment updatePayment(Payment payment) {
        log.info("Updating payment with ID: {}", payment.getPaymentId());
        Payment existingPayment = paymentRepo.findById(payment.getPaymentId()).orElseThrow(() -> {
            log.warn("Payment not found for update with ID: {}", payment.getPaymentId());
            return new RuntimeException("Payment not found with ID: " + payment.getPaymentId());
        });
        existingPayment.setStatus(Status.SUCCESS);
        existingPayment.setMethod(payment.getMethod());
        existingPayment.setTime(LocalDateTime.now());
        log.info("Payment updated successfully with ID: {}", payment.getPaymentId());
        return paymentRepo.save(existingPayment);
    }

    @Override
    public Payment getPaymentByRideId(int rideId) {
        log.info("Fetching payment by ride ID: {}", rideId);
        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> {
                    log.warn("Ride not found with ID: {}", rideId);
                    return new RuntimeException("Ride not found with ID: " + rideId);
                });
        return paymentRepo.findByRide(ride);
    }

    @Override
    public PaymentResponse getPaymentDetailsByPaymentId(int paymentId) {
        log.info("Fetching payment details for payment ID: {}", paymentId);
        Payment payment = paymentRepo.findById(paymentId).orElseThrow(() -> {
            log.warn("Payment not found with ID: {}", paymentId);
            return new RuntimeException("Payment not found with ID: " + paymentId);
        });
        Ride ride = payment.getRide();
        Driver driver = ride.getDriver();
        User user = ride.getUserRide();

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setUser(user);
        paymentResponse.setDriver(driver);

        log.info("Payment details fetched successfully for payment ID: {}", paymentId);
        return paymentResponse;
    }

    @Override
    public Ride getRideByPaymentId(int paymentId) {
        log.info("Fetching ride details for payment ID: {}", paymentId);
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> {
                    log.warn("Payment not found with ID: {}", paymentId);
                    return new RuntimeException("Payment is not found for this Id " + paymentId);
                });
        return payment.getRide();
    }
}