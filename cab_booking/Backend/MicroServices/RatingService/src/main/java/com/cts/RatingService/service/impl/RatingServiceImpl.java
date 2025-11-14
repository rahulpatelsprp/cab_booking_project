package com.cts.RatingService.service.impl;


import com.cts.RatingService.controller.UserController;
import com.cts.RatingService.dto.RatingUserAndDriverIdDto;
import com.cts.RatingService.dto.UserDTO;
import com.cts.RatingService.entity.Rating;
import com.cts.RatingService.repo.RatingRepo;
import com.cts.RatingService.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private UserController userController;

    @Override
    public Rating saveRating(int rideId, Rating rating, String token) {
        log.info("Saving rating for ride ID: {}", rideId);
        Rating newRating = new Rating();
        newRating.setRideId(rideId);
        newRating.setComment(rating.getComment());
        newRating.setScore(rating.getScore());
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
    public Rating saveRating(int riderId, RatingUserAndDriverIdDto ratingDto) {
        Rating rating=Rating.builder().comment(ratingDto.getComment())
                                .fromUserId(ratingDto.getFromUserId())
                                .toUserId(ratingDto.getToUserId())
                                .score(ratingDto.getScore())
                                .userId(ratingDto.getUserId())
                                .rideId(riderId)
                                .build();


        return ratingRepo.save(rating);
    }
    @Override
    public Double getAverageRating(String token) {
       UserDTO userDTO = userController.getUserByToken(token).orElseThrow(() -> new RuntimeException("User not found"));
        return ratingRepo.getAverageRating(userDTO.getUserId());
    }
}
