package com.cts.cab_booking.exception;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String msg) {
        super(msg);
    }
}
