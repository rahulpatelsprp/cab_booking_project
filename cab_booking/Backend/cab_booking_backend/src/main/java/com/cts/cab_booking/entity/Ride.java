package com.cts.cab_booking.entity;

import com.cts.cab_booking.helper.Status;
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
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int riderId;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference("ride-user")
    private User userRide;

    @Column(name = "ride-status")
    private Status status;
    @ManyToOne
    @JoinColumn(name = "driverId")
    @JsonBackReference("driver-ride")
    private Driver driver;

    @Column(name = "PickUpLocation")
    private String pickUpLocation;

    @Column(name = "DropOffLocation")
    private String dropOffLocation;

    private int startPin;

    @OneToOne(mappedBy = "ride", cascade = CascadeType.ALL)
    @JsonManagedReference("payment-ride")
    private Payment payment;


    @OneToMany(mappedBy = "rides", cascade = CascadeType.ALL)
    @JsonManagedReference("rating-ride")
    private List<Rating> rating;

}
