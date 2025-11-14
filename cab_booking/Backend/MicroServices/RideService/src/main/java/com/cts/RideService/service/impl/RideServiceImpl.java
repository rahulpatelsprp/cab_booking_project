package com.cts.RideService.service.impl;

import com.cts.RideService.controller.DriverController;
import com.cts.RideService.controller.PaymentController;
import com.cts.RideService.controller.UserController;
import com.cts.RideService.dto.*;
import com.cts.RideService.entity.Ride;
import com.cts.RideService.exception.*;
import com.cts.RideService.repo.RideRepo;
import com.cts.RideService.service.RideService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
public class RideServiceImpl implements RideService {


    private final RideRepo rideRepo;


    private final UserController userController;

    private final PaymentController paymentController;

    private final DriverController driverController;
    @Autowired
    public RideServiceImpl(RideRepo rideRepo, UserController userController, PaymentController paymentController, DriverController driverController) {
        this.rideRepo = rideRepo;
        this.userController = userController;
        this.paymentController = paymentController;
        this.driverController = driverController;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Ride saveRide(String token, RideRegister newRide) {
        log.info("Creating new ride for pickup: {} to drop-off: {}", newRide.getPickUpLocation(), newRide.getDropOffLocation());

        int pin = (int) (Math.random() * 9000 + 1000);
        Ride ride = new Ride();
        ride.setPickUpLocation(newRide.getPickUpLocation());
        ride.setDropOffLocation(newRide.getDropOffLocation());
        ride.setStartPin(pin);
        ride.setStatus(Status.PENDING);

        UserDTO user;
        try {
            user = userController.getUserByToken(token)
                    .orElseThrow(() -> new UserNotFoundException("User not found for token."));
        } catch (FeignException e) {
            log.error("Feign failed fetching user details: {}", e.getMessage());
            throw new MicroserviceCommunicationException("Failed to connect to User Service.", e);
        }

        ride.setUserId(user.getUserId());
        Ride savedRide = rideRepo.save(ride);
        log.info("Ride saved with ID: {}", savedRide.getRiderId());

        PaymentDTO payment = new PaymentDTO();
        payment.setAmount(newRide.getRate());
        payment.setRideId(savedRide.getRiderId());
        payment.setTime(LocalDateTime.now());
        payment.setUserId(ride.getUserId());

        PaymentDTO paymentDTO;
        try {
            ResponseEntity<PaymentDTO> response = paymentController.savePayment(payment);
            paymentDTO = response.getBody();
        } catch (FeignException e) {
            log.error("Feign failed initializing payment: {}", e.getMessage());
            throw new PaymentServiceException("Failed to connect to Payment Service.");
        }

        if (paymentDTO == null || paymentDTO.getPaymentId() == null) {
            String errorMsg = "Payment initialization failed. Received null or incomplete response.";
            log.error(errorMsg);
            throw new PaymentServiceException(errorMsg);
        }

        log.info("Payment initialized with ID: {}", paymentDTO.getPaymentId());

        savedRide.setPaymentId(paymentDTO.getPaymentId());
        return rideRepo.save(savedRide);
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
            return new RideNotFoundException("Ride not found with ID: " + id);
        });
    }

    @Override
    public Ride deleteRideById(int id) {
        log.info("Attempting to delete ride with ID: {}", id);
        Ride ride = rideRepo.findById(id).orElseThrow(() -> {
            log.warn("Ride not found for deletion with ID: {}", id);
            return new RideNotFoundException("Ride not found for deletion.");
        });

        rideRepo.delete(ride);
        log.info("Ride deleted successfully with ID: {}", id);
        return ride;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Ride updateRide(String token, Ride ride) {
        log.info("Updating ride status for ride ID: {}", ride.getRiderId());

        Ride existingRide = getRideById(ride.getRiderId());
        UserDTO driverDTO;

        try {
            driverDTO = userController.getUserByToken(token)
                    .orElseThrow(() -> new UserNotFoundException("Driver not found for token."));
        } catch (FeignException e) {
            log.error("Feign failed fetching driver details: {}", e.getMessage());
            throw new MicroserviceCommunicationException("Failed to connect to User Service.", e);
        }

        existingRide.setStatus(Status.ACCEPTED);
        existingRide.setDriverId(driverDTO.getUserId());
        log.info("Ride accepted by driver ID: {}", driverDTO.getUserId());

        return rideRepo.save(existingRide);
    }

    @Override
    public List<Ride> getAllRideByUserId(String token) {
        UserDTO userDTO;
        try {
            userDTO = userController.getUserByToken(token)
                    .orElseThrow(() -> new UserNotFoundException("User not found for token."));
        } catch (FeignException e) {
            log.error("Feign failed fetching user details: {}", e.getMessage());
            throw new MicroserviceCommunicationException("Failed to connect to User Service.", e);
        }
        return rideRepo.findByUserIdOrderByRiderIdDesc(userDTO.getUserId());
    }

    @Override
    public List<Ride> getAllRideByDriverId(String token) {
        UserDTO userDTO;
        try {
            userDTO = userController.getUserByToken(token)
                    .orElseThrow(() -> new UserNotFoundException("Driver not found for token."));
        } catch (FeignException e) {
            log.error("Feign failed fetching driver details: {}", e.getMessage());
            throw new MicroserviceCommunicationException("Failed to connect to User Service.", e);
        }
        return rideRepo.findByDriverIdOrderByRiderIdDesc(userDTO.getUserId());
    }

    @Override
    public List<Ride> getRideByDriverAndUser(int driverId, int userId) {
        log.info("Fetching rides for driver ID: {} and user ID: {}", driverId, userId);
        return rideRepo.findByDriverIdAndUserId(driverId, userId);
    }

    @Override
    public List<Ride> getAllRideBYStatus(Status status) {
        log.info("Fetching all rides with status: {}", status);
        return rideRepo.findByStatus(status);
    }

    @Transactional
    @Override
    public Ride changeRideStatusByRiderId(Status status, int riderId) {
        log.info("Changing ride status to {} for ride ID: {}", status, riderId);
        Ride ride = rideRepo.findById(riderId)
                .orElseThrow(() -> {
                    log.error("Ride not found with ID: {}", riderId);
                    return new RideNotFoundException("Ride not found with ID: " + riderId);
                });
        ride.setStatus(status);
        log.info("Ride status updated successfully for ride ID: {}", riderId);
        return rideRepo.save(ride);
    }

    @Override
    public UserDTO getUserByRiderId(int riderId) {
        Ride ride = rideRepo.findById(riderId).orElseThrow(() -> new RideNotFoundException("Ride not found"));
        int userId = ride.getUserId();

        try {
            return userController.getUserDetailsByUserId(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId));
        } catch (FeignException e) {
            log.error("Feign failed fetching user details: {}", e.getMessage());
            throw new MicroserviceCommunicationException("Failed to connect to User Service.", e);
        }
    }

    @Override
    public DriverDTO getDriverByRiderId(int riderId) {
        Ride ride = rideRepo.findById(riderId).orElseThrow(() -> new RideNotFoundException("Ride not found"));
        int driverId = ride.getDriverId();

        if (driverId == 0) {
            throw new DriverServiceException("Ride has not been accepted by a driver yet.");
        }

        try {
            ResponseEntity<DriverDTO> response = driverController.getDriverByRiderId(driverId);
            DriverDTO driverDTO = response.getBody();

            if (driverDTO == null) {
                throw new DriverServiceException("Driver not found for ID: " + driverId);
            }
            return driverDTO;
        } catch (FeignException e) {
            log.error("Feign communication failed while fetching driver details: {}", e.getMessage());
            throw new MicroserviceCommunicationException("Failed to connect to Driver Service.", e);
        }
    }


        @Override
        public Ride getPendingRideOfCurrentUser(String token) {
            UserDTO tempUser = userController.getUserByToken(token).orElseThrow(() -> new UserNotFoundException("User Not Found"));
            Ride pendingRide = rideRepo.findByStatusAndUserId(Status.PENDING, tempUser.getUserId());
            if (pendingRide == null) {
                return rideRepo.findByStatusAndUserId(Status.ACCEPTED, tempUser.getUserId());
            }
            return pendingRide;
        }

}