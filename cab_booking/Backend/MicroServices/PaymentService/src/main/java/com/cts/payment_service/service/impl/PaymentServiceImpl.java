package com.cts.payment_service.service.impl;


import com.cts.payment_service.controller.RideControllerFeign;
import com.cts.payment_service.dto.RideDTO;
import com.cts.payment_service.dto.Status;
import com.cts.payment_service.dto.PaymentResponse;
import com.cts.payment_service.entity.Payment;
import com.cts.payment_service.exception.PaymentDetailsNotFoundException;
import com.cts.payment_service.repo.PaymentRepo;
import com.cts.payment_service.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.awt.color.ProfileDataException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    RideControllerFeign rideController;

    @Override
    public Payment savePayment(Payment payment) {
        log.info("Saving new payment for ride ID: {}", payment.getRideId());
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

        return paymentRepo.findByRideId(rideId).orElseThrow(()->new PaymentDetailsNotFoundException("For that ride Id payment is Not found "+rideId));
    }

    @Override
    public PaymentResponse getPaymentDetailsByPaymentId(int paymentId) {
        log.info("Fetching payment details for payment ID: {}", paymentId);
        Payment payment = paymentRepo.findById(paymentId).orElseThrow(() -> {
            log.warn("Payment not found with ID: {}", paymentId);
            return new RuntimeException("Payment not found with ID: " + paymentId);
        });
//        Ride ride = payment.getRide();
//        Driver driver = ride.getDriver();
//        User user = ride.getUserRide();

          RideDTO rideDTO=rideController.getRideStatusById(payment.getRideId());
          if(rideDTO==null){
              throw new RuntimeException("Ride not found with ID: "+payment.getRideId());
          }
        PaymentResponse paymentResponse = new PaymentResponse();
         paymentResponse.setDriverId(rideDTO.getDriverId());
         paymentResponse.setUserId(rideDTO.getUserId());
        log.info("Payment details fetched successfully for payment ID: {}", paymentId);
        return paymentResponse;
    }

   @Override
   public Optional<Integer> getRideIdByPaymentId(int paymentId) {
       log.info("Fetching ride details for payment ID: {}", paymentId);
       Payment payment = paymentRepo.findById(paymentId)
               .orElseThrow(() -> {
                   log.warn("Payment not found with ID: {}", paymentId);
                   return new RuntimeException("Payment is not found for this Id " + paymentId);
               });
       return Optional.of(payment.getRideId());
   }
}