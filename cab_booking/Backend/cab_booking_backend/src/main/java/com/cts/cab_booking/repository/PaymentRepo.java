package com.cts.cab_booking.repository;

import com.cts.cab_booking.entity.Payment;
import com.cts.cab_booking.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    Payment findByRide(Ride ride);
}
