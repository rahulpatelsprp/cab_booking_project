package com.cts.RideService.dto;

// import jakarta.persistence.Column;

import lombok.*;

@Getter
@Setter
@Builder
public class RideRegister {
    private String pickUpLocation;
    private String dropOffLocation;
    private int rate;
}
