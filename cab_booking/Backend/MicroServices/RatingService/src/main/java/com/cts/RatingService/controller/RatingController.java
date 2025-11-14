package com.cts.RatingService.controller;


import com.cts.RatingService.dto.RatingUserAndDriverIdDto;
import com.cts.RatingService.dto.UserDTO;
import com.cts.RatingService.dto.UserIdAndDriverIdDto;
import com.cts.RatingService.entity.Rating;
import com.cts.RatingService.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/ratings")
public class    RatingController {

    private final RatingService ratingService;
    private final UserController userController;

    private final RideController rideController;
    @Autowired
    public RatingController(RatingService ratingService, UserController userController, RideController rideController) {
        this.ratingService = ratingService;
        this.userController = userController;
        this.rideController = rideController;
    }

    @PostMapping("/{riderId}")
    public ResponseEntity<?> createRating(@PathVariable int riderId, @RequestBody Rating rating, @RequestHeader("Authorization") String authHeader) {
        log.info("Rating is saving.........");
        Optional<UserDTO> userDTO= userController.getUserByToken(authHeader);
        if(userDTO.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        int userId=userDTO.get().getUserId();
       UserIdAndDriverIdDto rideDetails= rideController.getRideDetails(riderId);
        RatingUserAndDriverIdDto ratingDto=RatingUserAndDriverIdDto.builder().
                                userId(userId).fromUserId(rideDetails.getUserId()).
                toUserId(rideDetails.getDriverId()!=0?rideDetails.getDriverId():0).
                               Comment(rating.getComment()).score(rating.getScore()).build();

        return ResponseEntity.ok(ratingService.saveRating(riderId, ratingDto));
    }
    @GetMapping
    public ResponseEntity<?> getRatings() {
        return ResponseEntity.ok(ratingService.getAllRating());
    }

    @GetMapping("/my")
    public Double getAverageRating(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return ratingService.getAverageRating(token);
    }
}
