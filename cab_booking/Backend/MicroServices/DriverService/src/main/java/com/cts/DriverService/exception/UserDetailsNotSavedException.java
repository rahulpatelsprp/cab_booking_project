package com.cts.DriverService.exception;

public class UserDetailsNotSavedException extends RuntimeException {
    public UserDetailsNotSavedException(String userIsNotSaved) {
        super(userIsNotSaved);
    }
}
