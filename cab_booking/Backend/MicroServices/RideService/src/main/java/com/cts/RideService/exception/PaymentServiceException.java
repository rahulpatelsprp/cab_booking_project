package com.cts.RideService.exception;

import feign.FeignException;

public class PaymentServiceException extends RuntimeException {
    public PaymentServiceException(String s) {
        super(s);
    }
}
