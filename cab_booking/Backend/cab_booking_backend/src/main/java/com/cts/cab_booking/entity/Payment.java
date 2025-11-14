package com.cts.cab_booking.entity;

import com.cts.cab_booking.helper.Method;
import com.cts.cab_booking.helper.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PaymentId")
    private int paymentId;

    @OneToOne
    @JoinColumn(name = "RideId")
    @JsonBackReference("payment-ride")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "UserId")
    @JsonBackReference("payment-user")
    private User userPayment;

    @Column(name = "Amount")
    private int amount;

    @Column(name = "Method")
    private Method method;

    @Column(name = "Status")
    private Status status;

    @Column(name = "TimeStamp")
    private LocalDateTime time;
}
