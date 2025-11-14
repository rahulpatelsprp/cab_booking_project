package com.cts.cab_booking.service.impl;

import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.exception.RideNotFoundException;
import com.cts.cab_booking.helper.Role;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.Rating;
import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.exception.UserNotFoundException;
import com.cts.cab_booking.helper.Status;
import com.cts.cab_booking.repository.RatingRepo;
import com.cts.cab_booking.securityConfig.JwtUtil;
import com.cts.cab_booking.service.DriverService;
import com.cts.cab_booking.service.RideService;
import com.cts.cab_booking.service.UserService;

import org.junit.jupiter.api.DisplayName;

import org.mockito.ArgumentCaptor;


@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepo ratingRepo;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RideService rideService;

    @Mock
    private UserService userService;

    @Mock
    private DriverService driverService;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private User passenger;
    private User driverUser;
    private Driver driver;
    private Ride ride;
    private Rating newRatingRequest;
    private String token = "mock-jwt-token";
    private int rideId = 1;

    // Helper method to manually create User objects
    private User createUser(int id, String email, Role role, long phone) {
        User user = new User();
        user.setUserId(id);
        user.setEmail(email);
        user.setRole(role);
        user.setPhone(phone);
        return user;
    }

    // Helper method to manually create Rating objects
    private Rating createRating(int id, int score, String comment) {
        Rating rating = new Rating();
        rating.setRatingId(id);
        rating.setScore(score);
        rating.setComment(comment);
        return rating;
    }

    @BeforeEach
    void setUp() {
        // Mock Passenger User
        passenger = createUser(101, "passenger@test.com", Role.USER, 1234567890L);

        // Mock Driver User
        driverUser = createUser(102, "driver@test.com", Role.DRIVER, 9876543210L);

        // Mock Driver Entity
        driver = new Driver();
        driver.setDriverId(1);
        driver.setPhone(driverUser.getPhone());

        // Mock Ride
        ride = new Ride();
        ride.setRiderId(rideId);
        ride.setUserRide(passenger);
        ride.setDriver(driver);
        ride.setStatus(Status.COMPLETED);

        // Mock Rating request object
        newRatingRequest = createRating(0, 5, "Great service!");
    }

    // --- saveRating Tests (Focus on Rater/Recipient Logic) ---

    @Test
    @DisplayName("Passenger rates Driver successfully")
    void saveRating_PassengerRatesDriver_Success() {
        // Arrange
        when(rideService.getRideById(rideId)).thenReturn(ride);
        when(jwtUtil.extractUsername(token)).thenReturn(passenger.getEmail());
        when(userService.getUserByEmail(passenger.getEmail())).thenReturn(Optional.of(passenger));
        when(userService.getUserByPhone(driver.getPhone())).thenReturn(Optional.of(driverUser));

        // Manual object creation for the mocked return object
        Rating savedRating = createRating(1, newRatingRequest.getScore(), newRatingRequest.getComment());
        when(ratingRepo.save(any(Rating.class))).thenReturn(savedRating);

        // Act
        Rating result = ratingService.saveRating(rideId, newRatingRequest, token);

        // Assert
        ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepo).save(ratingCaptor.capture());

        Rating capturedRating = ratingCaptor.getValue();

        assertEquals(5, result.getScore());
        // Passenger is rating, so 'from' is passenger, 'to' is driver's user object
        assertEquals(passenger.getUserId(), capturedRating.getFromUserId().getUserId(), "Rater should be the passenger");
        assertEquals(driverUser.getUserId(), capturedRating.getToUserId().getUserId(), "Recipient should be the driver's User");

        verify(userService, times(1)).getUserByPhone(driver.getPhone());
    }

    @Test
    @DisplayName("Driver rates Passenger successfully")
    void saveRating_DriverRatesPassenger_Success() {
        // Arrange
        User currentRater = driverUser;
        String driverToken = "driver-jwt-token";

        when(rideService.getRideById(rideId)).thenReturn(ride);
        when(jwtUtil.extractUsername(driverToken)).thenReturn(currentRater.getEmail());
        when(userService.getUserByEmail(currentRater.getEmail())).thenReturn(Optional.of(currentRater));

        // Manual object creation for the mocked return object
        Rating savedRating = createRating(1, newRatingRequest.getScore(), newRatingRequest.getComment());
        when(ratingRepo.save(any(Rating.class))).thenReturn(savedRating);

        // Act
        Rating result = ratingService.saveRating(rideId, newRatingRequest, driverToken);

        // Assert
        ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepo).save(ratingCaptor.capture());

        Rating capturedRating = ratingCaptor.getValue();

        assertEquals(5, result.getScore());
        // Driver is rating, so 'from' is driver, 'to' is passenger
        assertEquals(currentRater.getUserId(), capturedRating.getFromUserId().getUserId(), "Rater should be the driver");
        assertEquals(passenger.getUserId(), capturedRating.getToUserId().getUserId(), "Recipient should be the passenger");

        verify(userService, never()).getUserByPhone(anyLong());
    }

    @Test
    @DisplayName("saveRating throws UserNotFoundException for invalid rater token")
    void saveRating_InvalidRaterToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalid-token";
        when(rideService.getRideById(rideId)).thenReturn(ride);
        when(jwtUtil.extractUsername(invalidToken)).thenReturn("unknown@test.com");
        when(userService.getUserByEmail("unknown@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                ratingService.saveRating(rideId, newRatingRequest, invalidToken));
    }

    @Test
    @DisplayName("saveRating throws UserNotFoundException when Driver's User is not found (Passenger rates)")
    void saveRating_PassengerRates_DriverUserNotFound_ThrowsException() {
        // Arrange
        when(rideService.getRideById(rideId)).thenReturn(ride);
        when(jwtUtil.extractUsername(token)).thenReturn(passenger.getEmail());
        when(userService.getUserByEmail(passenger.getEmail())).thenReturn(Optional.of(passenger));
        when(userService.getUserByPhone(driver.getPhone())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                ratingService.saveRating(rideId, newRatingRequest, token));
    }

    // --- CRUD Tests ---

    @Test
    @DisplayName("getAllRating returns list of all ratings")
    void getAllRating_Success() {
        // Arrange
        Rating r1 = createRating(1, 5, "ok");
        Rating r2 = createRating(2, 4, "good");
        List<Rating> expectedRatings = Arrays.asList(r1, r2);
        when(ratingRepo.findAll()).thenReturn(expectedRatings);

        // Act
        List<Rating> result = ratingService.getAllRating();

        // Assert
        assertEquals(2, result.size());
        verify(ratingRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("getRatingById returns Rating when found")
    void getRatingById_Found_Success() {
        // Arrange
        int id = 1;
        Rating expected = createRating(id, 5, "great");
        when(ratingRepo.findById(id)).thenReturn(Optional.of(expected));

        // Act
        Rating result = ratingService.getRatingById(id);

        // Assert
        assertEquals(expected.getRatingId(), result.getRatingId());
        verify(ratingRepo, times(1)).findById(id);
    }
    

    @Test
    @DisplayName("deleteRatingById deletes and returns Rating when found")
    void deleteRatingById_Found_Success() {
        // Arrange
        int id = 1;
        Rating ratingToDelete = createRating(id, 5, "delete");
        when(ratingRepo.findById(id)).thenReturn(Optional.of(ratingToDelete));

        // Act
        Rating result = ratingService.deleteRatingById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getRatingId());
        verify(ratingRepo, times(1)).delete(ratingToDelete);
    }

    @Test
    @DisplayName("deleteRatingById returns null when Rating not found")
    void deleteRatingById_NotFound_ReturnsNull() {
        // Arrange
        int id = 99;
        when(ratingRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        Rating result = ratingService.deleteRatingById(id);

        // Assert
        assertNull(result);
        verify(ratingRepo, never()).delete(any(Rating.class));
    }

    @Test
    @DisplayName("updateRating calls repository save")
    void updateRating_Success() {
        // Arrange
        Rating ratingToUpdate = createRating(1, 3, "Needs improvement");

        // Act
        ratingService.updateRating(ratingToUpdate);

        // Assert
        verify(ratingRepo, times(1)).save(ratingToUpdate);
    }
}