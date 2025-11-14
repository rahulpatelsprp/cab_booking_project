package com.cts.cab_booking.helper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverResponse {
    private String name;
    private long phone;
    private String email;
    private String vehicleDetails;
    private String licenseNumber;
    private int driverSinceYear;
}
