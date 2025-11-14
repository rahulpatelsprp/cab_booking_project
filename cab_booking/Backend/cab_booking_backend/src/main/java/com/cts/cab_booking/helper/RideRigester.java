package com.cts.cab_booking.helper;

// import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideRigester {
    private String pickUpLocation;
    private String dropOffLocation;
    private int rate;
}
