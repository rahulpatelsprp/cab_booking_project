package com.cts.RideService.entity;


import com.cts.RideService.dto.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int riderId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "ride-status")
    private Status status;

    private int driverId;

    @Column(name = "PickUpLocation")
    private String pickUpLocation;

    @Column(name = "DropOffLocation")
    private String dropOffLocation;

    @Column(name = "start_pin")
    private int startPin;


    private int  paymentId;


//    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(name = "ride_ratings", joinColumns = @JoinColumn(name = "ride_id"))
//    @Column(name = "rating")
//    private List<Integer> ratings;
//
//
//
//    public void setRatingId(int ratingId) {
//        if(ratings==null){
//            ratings=new ArrayList<>();
//        }
//       this.ratings.add(ratingId);
//    }
}
