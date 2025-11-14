package com.cts.cab_booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    private int driverId; // same as userId

    @OneToOne
    @MapsId // tells JPA to use the same ID as the User
    @JoinColumn(name = "driverId")
    @JsonBackReference("user-driver")
    private User user;


    @Column(name = "Name")
    private String name;

    @Column(name = "Phone")
    private long phone;

    @Column(name = "LicenseNumber")
    private String licenseNumber;

    @Column(name = "VehicleDetails")
    private String vehicleDetails;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    @JsonManagedReference("driver-ride")
    private List<Ride> rides;


}
