package com.cts.DriverService.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "phone_unique",
                columnNames = "phone"
        )
)
public class Driver {
    @Id
    private int driverId;
    @Column(name = "Name")
    private String name;
    @Column(name = "Phone")
    private long phone;
    @Column(name = "LicenseNumber")
    private String licenseNumber;
    @Column(name = "VehicleDetails")
    private String vehicleDetails;
}
