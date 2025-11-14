package com.cts.RideService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverDTO {
    private String name;
    private long phone;
    private String licenseNumber;
    private String vehicleDetails;
}
