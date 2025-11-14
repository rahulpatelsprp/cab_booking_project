package com.cts.payment_service.exception;

public class PaymentDetailsNotFoundException extends RuntimeException{
    public PaymentDetailsNotFoundException(String message) {
        super(message);
    }
}
