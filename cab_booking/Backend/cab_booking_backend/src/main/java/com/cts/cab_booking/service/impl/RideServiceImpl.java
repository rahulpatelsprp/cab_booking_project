package com.cts.cab_booking.service.impl;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.Payment;
import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.exception.RideNotFoundException;
import com.cts.cab_booking.exception.UserNotFoundException;
import com.cts.cab_booking.helper.RideRigester;
import com.cts.cab_booking.helper.Status;
import com.cts.cab_booking.repository.RideRepo;
import com.cts.cab_booking.repository.UserRepo;
import com.cts.cab_booking.securityConfig.JwtUtil;
import com.cts.cab_booking.service.DriverService;
import com.cts.cab_booking.service.PaymentService;
import com.cts.cab_booking.service.RideService;
import com.cts.cab_booking.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class RideServiceImpl implements RideService {


    private final RideRepo rideRepo;
    private final JwtUtil jwtUtil;
    private final DriverService driverService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final UserRepo userRepo;

    @Autowired
    public RideServiceImpl(RideRepo rideRepo, JwtUtil jwtUtil, DriverService driverService, UserService userService, PaymentService paymentService, UserRepo userRepo) {
        this.rideRepo = rideRepo;
        this.jwtUtil = jwtUtil;
        this.driverService = driverService;
        this.userService = userService;
        this.paymentService = paymentService;
        this.userRepo = userRepo;
    }

    @Override
    public Ride saveRide(String token, RideRigester newRide) {
        log.info("Creating new ride for pickup: {} to drop-off: {}", newRide.getPickUpLocation(), newRide.getDropOffLocation());

        int pin = (int) (Math.random() * 9000 + 1000);
        Ride ride = new Ride();
        ride.setPickUpLocation(newRide.getPickUpLocation());
        ride.setDropOffLocation(newRide.getDropOffLocation());
        ride.setStartPin(pin);
        ride.setStatus(Status.PENDING);

        String username = jwtUtil.extractUsername(token);
        Optional<User> user = userService.getUserByEmail(username);
        user.ifPresent(ride::setUserRide);

        Ride savedRide = rideRepo.save(ride);
        log.info("Ride saved with ID: {}", savedRide.getRiderId());

        Payment payment = new Payment();
        payment.setStatus(Status.PENDING);
        payment.setAmount(newRide.getRate());
        user.ifPresent(payment::setUserPayment);
        payment.setRide(savedRide);
        payment.setTime(LocalDateTime.now());

        paymentService.savePayment(payment);
        log.info("Payment initialized for ride ID: {}", savedRide.getRiderId());

        return savedRide;
    }

    @Override
    public List<Ride> getAllRide() {
        log.info("Fetching all rides.");
        return rideRepo.findAll(Sort.by(Sort.Direction.DESC, "riderId"));
    }

    @Override
    public Ride getRideById(int id) {
        log.info("Fetching ride by ID: {}", id);
        return rideRepo.findById(id).orElseThrow(() -> {
            log.error("Ride not found with ID: {}", id);
            return new RideNotFoundException("Ride not found");
        });
    }

    @Override
    public Ride deleteRideById(int id) {
        log.info("Attempting to delete ride with ID: {}", id);
        Optional<Ride> ride = rideRepo.findById(id);
        if (ride.isPresent()) {
            rideRepo.delete(ride.get());
            log.info("Ride deleted successfully with ID: {}", id);
            return ride.get();
        } else {
            log.warn("Ride not found for deletion with ID: {}", id);
            return null;
        }
    }

    @Override
    public Ride updateRide(String token, Ride ride) {
        log.info("Updating ride status for ride ID: {}", ride.getRiderId());

        String username = jwtUtil.extractUsername(token);
        Ride existingRide = getRideById(ride.getRiderId());

        Optional<User> user = userService.getUserByEmail(username);
        Driver driver = driverService.getDriverByPhone(user.get().getPhone());

        existingRide.setStatus(Status.ACCEPTED);
        existingRide.setDriver(driver);

        log.info("Ride accepted by driver with phone: {}", driver.getPhone());
        return rideRepo.save(existingRide);
    }

    @Override
    public List<Ride> getAllRideByUserId(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepo.findByEmail(username).orElseThrow(() -> {
            log.warn("User is not found");
            return new UserNotFoundException("User is not found");
        });
        log.info("Fetching all rides for user ID: {}", user.getUserId());
        return rideRepo.findByUserRideUserIdOrderByRiderIdDesc(user.getUserId());
    }

    @Override
    public List<Ride> getAllRideByDriverId(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepo.findByEmail(username).orElseThrow(() -> {
            log.warn("User is not found");
            return new UserNotFoundException("User is not found");
        });
        log.info("Fetching all rides for driver ID: {}", user.getDriver());
        return rideRepo.findByDriverDriverIdOrderByRiderIdDesc(user.getDriver().getDriverId());
    }

    @Override
    public List<Ride> getRideByDriverAndUser(int driverId, int userId) {
        log.info("Fetching rides for driver ID: {} and user ID: {}", driverId, userId);
        return rideRepo.findByDriverDriverIdAndUserRideUserId(driverId, userId);
    }

    @Override
    public List<Ride> getAllRideBYStatus(Status status) {
        log.info("Fetching all rides with status: {}", status);
        return rideRepo.findByStatus(status);
    }

    @Override
    public Ride changeRideStatusByRiderId(Status status, int riderId) {
        log.info("Changing ride status to {} for ride ID: {}", status, riderId);
        Ride ride = rideRepo.findById(riderId)
                .orElseThrow(() -> {
                    log.error("Ride not found with ID: {}", riderId);
                    return new RideNotFoundException("Ride not found");
                });
        ride.setStatus(status);
        log.info("Ride status updated successfully for ride ID: {}", riderId);
        return rideRepo.save(ride);
    }

    @Override
    public Ride getPendingRideOfCurrentUser(String token) {
        String username = jwtUtil.extractUsername(token);
        User tempUser = userRepo.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Ride pendingRide = rideRepo.getRideByStatusAndUserRideUserId(Status.PENDING, tempUser.getUserId());
        if (pendingRide == null) {
            return rideRepo.getRideByStatusAndUserRideUserId(Status.ACCEPTED, tempUser.getUserId());
        }
        return pendingRide;
    }
}