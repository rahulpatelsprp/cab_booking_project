package com.cts.cab_booking.service;

import com.cts.cab_booking.entity.Payment;
import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.helper.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    abstract Payment savePayment(Payment payment);

    abstract List<Payment> getAllPayment();

    abstract Payment getPaymentById(int id);

    abstract Payment deletePaymentById(int id);

    abstract Payment updatePayment(Payment payment);

    Payment getPaymentByRideId(int rideId);

    PaymentResponse getPaymentDetailsByPaymentId(int paymentId);

    Ride getRideByPaymentId(int paymentId);
}
