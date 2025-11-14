package com.cts.cab_booking.controller;

import com.cts.cab_booking.entity.Rating;
import com.cts.cab_booking.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/{riderId}")
    public ResponseEntity<?> createRating(@PathVariable int riderId, @RequestBody Rating rating, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        log.info("Rating is saving.........");
        return ResponseEntity.ok(ratingService.saveRating(riderId, rating, token));
    }

    @GetMapping("/{userID}")
    public List<Rating> getRatingsByUserId(@PathVariable Integer userId) {
        return ratingService.getRatingsByUserId(userId);
    }

    @GetMapping
    public List<Rating> getRatingsByToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return ratingService.getRatingsByToken(token);
    }

    @GetMapping("/my")
    public Double getAverageRating(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return ratingService.getAverageRating(token);
    }
}
