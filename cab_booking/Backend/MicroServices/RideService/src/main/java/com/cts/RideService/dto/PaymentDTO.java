package com.cts.RideService.dto;

import java.time.LocalDateTime;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO{
    private Integer paymentId;
    private Integer amount;
    private Integer rideId;
    private Integer userId;
    private String method;
    private Status status;
    private LocalDateTime time;

}