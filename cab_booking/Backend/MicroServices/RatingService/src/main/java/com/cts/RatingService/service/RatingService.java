package com.cts.RatingService.service;


import com.cts.RatingService.dto.RatingUserAndDriverIdDto;
import com.cts.RatingService.entity.Rating;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {
    abstract Rating saveRating(int riderId, Rating rating, String token);

    abstract List<Rating> getAllRating();

    abstract Rating getRatingById(int id);

    abstract Rating deleteRatingById(int id);

    abstract void updateRating(Rating rating);

    abstract Rating saveRating(int riderId, RatingUserAndDriverIdDto ratingDto);

    Double getAverageRating(String token);
}
