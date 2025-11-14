package com.cts.RatingService.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @Column(name = "RatingId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ratingId;

    private int rideId;

    private int fromUserId;

    private int toUserId;

    @Column(name = "Score")
    private int score;

    @Column(name = "Comment")
    private String comment;

   private int userId;


}
