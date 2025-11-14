package com.cts.cab_booking.entity;


import com.cts.cab_booking.helper.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"userId", "email", "phone"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserId")
    private int userId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "Phone")
    private long phone;

    @Column(name = "role")
    private Role role;

    @Column(name = "PasswordHash")
    private String passwordHash;

    @OneToMany(mappedBy = "userRide", cascade = CascadeType.ALL)
    @JsonManagedReference("ride-user")
    private List<Ride> rides;

    @OneToMany(mappedBy = "userPayment", cascade = CascadeType.ALL)
    @JsonManagedReference("payment-user")
    private List<Payment> payment;

    @OneToMany(mappedBy = "fromUserId", cascade = CascadeType.ALL)
    @JsonManagedReference("rating-user")
    private List<Rating> fromRatings;

    @OneToMany(mappedBy = "toUserId", cascade = CascadeType.ALL)
    @JsonManagedReference("rating-driver")
    private List<Rating> toRatings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-driver")
    private Driver driver;
}
