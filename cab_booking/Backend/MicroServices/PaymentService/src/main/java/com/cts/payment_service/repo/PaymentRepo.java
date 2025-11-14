package com.cts.payment_service.repo;

import com.cts.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {


    Optional<Payment> findByRideId(int rideId);
}
