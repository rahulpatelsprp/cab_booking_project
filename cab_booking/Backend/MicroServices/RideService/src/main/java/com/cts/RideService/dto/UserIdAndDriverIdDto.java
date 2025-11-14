package com.cts.RideService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserIdAndDriverIdDto {
    int userId;
    int driverId;
}
