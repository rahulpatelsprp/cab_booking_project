package com.cts.payment_service.dto;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Integer amount;
    private Integer rideId;
    private Integer userId;
    private String method;
}