package com.cts.cab_booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @ManyToOne
    @JoinColumn(name = "RideId")
    @JsonBackReference("rating-ride")
    private Ride rides;


    @ManyToOne
    @JoinColumn(name = "FromUserId")
    @JsonBackReference("rating-user") // Optional: Hide if not needed in response
    private User fromUserId;

    @ManyToOne
    @JoinColumn(name = "ToUserId")
    @JsonBackReference("rating-driver")
    private User toUserId;

    @Column(name = "Score")
    private int score;

    @Column(name = "Comment")
    private String comment;

//    @ManyToOne
//    @JoinColumn(name = "UserRating")
//     @JsonBackReference("rating-user")
//    private User userRating;

}
