package com.cts.DriverService.exception;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String msg) {
        super(msg);
    }
}
