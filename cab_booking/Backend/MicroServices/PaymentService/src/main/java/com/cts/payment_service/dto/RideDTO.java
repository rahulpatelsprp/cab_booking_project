package com.cts.payment_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RideDTO {
    int userId;
    int driverId;
}
