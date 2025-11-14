package com.cts.RideService.exception;

import feign.FeignException;

public class MicroserviceCommunicationException extends FeignException {
    public MicroserviceCommunicationException(String s,FeignException e) {
        super(e.status(), s);
    }
}
