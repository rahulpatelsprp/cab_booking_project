package com.cts.cab_booking.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

// AuthRequest.java
@Data
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
    // getters and setters
}
