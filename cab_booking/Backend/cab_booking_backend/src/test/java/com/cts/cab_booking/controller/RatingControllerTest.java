package com.cts.cab_booking.controller;

import com.cts.cab_booking.entity.Rating;
import com.cts.cab_booking.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Mock
    private RatingService ratingService; // Mock the dependency

    @InjectMocks
    private RatingController ratingController; // Inject the mock into the controller

    private Rating mockRequestRating;
    private Rating mockResponseRating;
    private final int RIDE_ID = 1;
    private final String JWT_TOKEN = "mock.jwt.token";
    private final String AUTH_HEADER = "Bearer " + JWT_TOKEN;

    @BeforeEach
    void setUp() {
        // Manual object creation for the request (avoiding builder)
        mockRequestRating = new Rating();
        mockRequestRating.setScore(5);
        mockRequestRating.setComment("Excellent ride!");

        // Manual object creation for the response (avoiding builder)
        mockResponseRating = new Rating();
        mockResponseRating.setRatingId(101);
        mockResponseRating.setScore(5);
        mockResponseRating.setComment("Excellent ride!");
    }

    @Test
    @DisplayName("createRating should strip 'Bearer ' and call service successfully")
    void createRating_Success() {
        // Arrange

        when(ratingService.saveRating(
                eq(RIDE_ID),
                any(Rating.class),
                eq(JWT_TOKEN) // **Crucial: Verifying the stripped token is passed**
        )).thenReturn(mockResponseRating);

        // Act
        ResponseEntity<?> response = ratingController.createRating(
                RIDE_ID,
                mockRequestRating,
                AUTH_HEADER
        );

        // Assert

        verify(ratingService).saveRating(eq(RIDE_ID), any(Rating.class), eq(JWT_TOKEN));

        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCode().value(), "Response status should be 200 OK");
        assertEquals(mockResponseRating, response.getBody(), "Response body should match the saved rating");
    }

    @Test
    @DisplayName("createRating should handle service failure and return Internal Server Error (or expected error)")
    void createRating_ServiceFailure() {

        when(ratingService.saveRating(
                eq(RIDE_ID),
                any(Rating.class),
                eq(JWT_TOKEN)
        )).thenThrow(new RuntimeException("Database connection lost"));

        assertThrows(RuntimeException.class, () ->
                ratingController.createRating(RIDE_ID, mockRequestRating, AUTH_HEADER)
        );

        verify(ratingService).saveRating(eq(RIDE_ID), any(Rating.class), eq(JWT_TOKEN));
    }
}