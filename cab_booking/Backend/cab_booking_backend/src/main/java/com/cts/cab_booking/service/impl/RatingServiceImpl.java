package com.cts.cab_booking.service.impl;

import com.cts.cab_booking.entity.Rating;
import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.exception.UserNotFoundException;
import com.cts.cab_booking.helper.Role;
import com.cts.cab_booking.repository.RatingRepo;
import com.cts.cab_booking.securityConfig.JwtUtil;
import com.cts.cab_booking.service.RatingService;
import com.cts.cab_booking.service.RideService;
import com.cts.cab_booking.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class RatingServiceImpl implements RatingService {

    private final RatingRepo ratingRepo;
    private final JwtUtil jwtUtil;
    private final RideService rideService;
    private final UserService userService;

    @Autowired
    public RatingServiceImpl(RatingRepo ratingRepo, JwtUtil jwtUtil, RideService rideService, UserService userService) {
        this.ratingRepo = ratingRepo;
        this.jwtUtil = jwtUtil;
        this.rideService = rideService;
        this.userService = userService;
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public Rating saveRating(int riderId, Rating rating, String token) {
        log.info("Saving rating for ride ID: {}", riderId);

        Ride ride = rideService.getRideById(riderId);
        String username = jwtUtil.extractUsername(token);
        User user = userService.getUserByEmail(username)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", username);
                    return new UserNotFoundException("User not found");
                });

        Rating newRating = new Rating();
        newRating.setRides(ride);
        newRating.setComment(rating.getComment());
        newRating.setScore(rating.getScore());

        if (user.getRole() == Role.DRIVER) {
            newRating.setToUserId(ride.getUserRide());
            newRating.setFromUserId(user);
            log.info("Driver rating a user.");
        } else {
            log.info("Driver rating not a user." + ride.getDriver());
            if (ride.getDriver() != null) {
                newRating.setToUserId(userService.getUserByPhone(ride.getDriver().getPhone())
                        .orElseThrow(() -> {
                            log.warn("Driver not found by phone: {}", ride.getDriver().getPhone());
                            return new UserNotFoundException("Driver not found by phone");
                        }));
            }


            newRating.setFromUserId(user);
            log.info("User rating a driver.");
        }

        log.info("Rating saved successfully.");
        return ratingRepo.save(newRating);
    }

    @Override
    public List<Rating> getAllRating() {
        log.info("Fetching all ratings.");
        return ratingRepo.findAll();
    }

    @Override
    public Rating getRatingById(int id) {
        log.info("Fetching rating by ID: {}", id);
        return ratingRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rating not found with ID: {}", id);
                    return new RuntimeException("Rating not found");
                });
    }

    @Override
    public Rating deleteRatingById(int id) {
        log.info("Attempting to delete rating with ID: {}", id);
        Optional<Rating> rating = ratingRepo.findById(id);
        if (rating.isPresent()) {
            ratingRepo.delete(rating.get());
            log.info("Rating deleted successfully with ID: {}", id);
            return rating.get();
        } else {
            log.warn("Rating not found for deletion with ID: {}", id);
            return null;
        }
    }

    @Override
    public void updateRating(Rating rating) {
        log.info("Updating rating with ID: {}", rating.getRatingId());
        ratingRepo.save(rating);
        log.info("Rating updated successfully.");
    }

    @Override
    public List<Rating> getRatingsByUserId(Integer userId) {
        return ratingRepo.findByToUserIdUserId(userId);
    }

    @Override
    public List<Rating> getRatingsByToken(String token) {
        User tempUser = userService.getUserByEmail(jwtUtil.extractUsername(token)).orElseThrow(() -> new UserNotFoundException("User not found"));
        return ratingRepo.findByToUserIdUserId(tempUser.getUserId());
    }

    @Override
    public Double getAverageRating(String token) {
        User tempUser = userService.getUserByEmail(jwtUtil.extractUsername(token)).orElseThrow(() -> new UserNotFoundException("User not found"));
        return ratingRepo.getAverageRating(tempUser.getUserId());
    }


}
