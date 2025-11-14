package com.cts.cab_booking.helper;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.User;
import lombok.Data;

@Data
public class PaymentResponse {
    private User user;
    private Driver driver;
}
