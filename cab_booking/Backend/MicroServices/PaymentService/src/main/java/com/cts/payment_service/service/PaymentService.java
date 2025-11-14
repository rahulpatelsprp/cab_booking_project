package com.cts.payment_service.service;


import com.cts.payment_service.dto.PaymentResponse;
import com.cts.payment_service.entity.Payment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PaymentService {
    abstract Payment savePayment(Payment payment);

    abstract List<Payment> getAllPayment();

    abstract Payment getPaymentById(int id);

    abstract Payment deletePaymentById(int id);

    abstract Payment updatePayment(Payment payment);

    Payment getPaymentByRideId(int rideId);

    PaymentResponse getPaymentDetailsByPaymentId(int paymentId);

    Optional<Integer> getRideIdByPaymentId(int paymentId);
}
