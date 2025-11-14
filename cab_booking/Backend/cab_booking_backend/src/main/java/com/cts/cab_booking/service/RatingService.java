package com.cts.cab_booking.service;

import com.cts.cab_booking.entity.Rating;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {
    abstract Rating saveRating(int riderId, Rating rating, String token);

    abstract List<Rating> getAllRating();

    abstract Rating getRatingById(int id);

    abstract Rating deleteRatingById(int id);

    abstract void updateRating(Rating rating);

	abstract List<Rating> getRatingsByUserId(Integer userId);

	abstract List<Rating> getRatingsByToken(String token);

	abstract Double getAverageRating(String token);
}
