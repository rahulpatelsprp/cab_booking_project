package com.cts.DriverService.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class DriverRegistration {
    private String name;
    private String email;
    private long phone;
    private String passwordHash;
    private String licenseNumber;
    private String vehicleDetails;
}
