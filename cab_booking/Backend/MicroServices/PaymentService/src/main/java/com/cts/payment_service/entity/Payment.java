package com.cts.payment_service.entity;



import com.cts.payment_service.dto.Status;
import com.cts.payment_service.dto.Method;
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
    private int rideId;
    private int userId;     
    @Column(name = "Amount")
    private int amount;

    @Column(name = "Method")
    private Method method;

    @Column(name = "Status")
    private Status status;

    @Column(name = "TimeStamp")
    private LocalDateTime time;
}
