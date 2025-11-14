package com.cts.cab_booking.controller;

import com.cts.cab_booking.entity.Driver;
import com.cts.cab_booking.entity.Ride;
import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.helper.RideRigester;
import com.cts.cab_booking.helper.Status;
import com.cts.cab_booking.securityConfig.JwtUtil;
import com.cts.cab_booking.service.DriverService;
import com.cts.cab_booking.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideControllerTest {

    // Use a real instance of JwtUtil to generate valid test tokens
    private final JwtUtil jwtUtil = new JwtUtil();
    @Mock
    private RideService rideService;
    @Mock
    private DriverService driverService;
    @InjectMocks
    private RideController rideController;

    private String userToken;
    private String userAuthHeader;
    private Ride mockRide;
    private User mockUser;
    private Driver mockDriver;

    // --- Utility Method to Generate Test Tokens ---

    /**
     * Generates a valid signed JWT for testing authorization headers.
     *
     * @param username The subject of the token (e.g., user ID or email).
     * @param role     The role to include in the token (e.g., "USER", "DRIVER").
     * @return The raw JWT string.
     */
    private String generateTestToken(String username, String role) {
        return jwtUtil.generateToken(username, role);
    }
    // ---------------------------------------------

    @BeforeEach
    void setUp() {
        // Generate a token for a mock user (e.g., ID 101) with the USER role
        userToken = generateTestToken("user@test.com", "USER");
        userAuthHeader = "Bearer " + userToken;

        mockRide = new Ride();
        mockRide.setRiderId(1);
        mockRide.setPickUpLocation("A");
        mockRide.setStatus(Status.PENDING);

        mockUser = new User();
        mockUser.setUserId(101);

        mockDriver = new Driver();
        mockDriver.setDriverId(202);
    }

    @Test
    void getAllRides_ShouldReturnListOfRides() {
        // Arrange
        List<Ride> expectedRides = Arrays.asList(mockRide, new Ride());
        when(rideService.getAllRide()).thenReturn(expectedRides);

        // Act
        List<Ride> actualRides = rideController.getAllRides();

        // Assert
        assertNotNull(actualRides);
        assertEquals(2, actualRides.size());
        verify(rideService, times(1)).getAllRide();
    }

    @Test
    void register_ShouldReturnOkWithSavedRide() {
        // Arrange
        RideRigester rideRigester = new RideRigester();
        // Token passed to service is the raw token string
        when(rideService.saveRide(userToken, rideRigester)).thenReturn(mockRide);

        // Act
        ResponseEntity<?> response = rideController.register(userAuthHeader, rideRigester);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRide, response.getBody());
        verify(rideService, times(1)).saveRide(userToken, rideRigester);
    }

    @Test
    void getAllRideByUserId_ShouldReturnListOfRides() {
        // Arrange
        List<Ride> expectedRides = Arrays.asList(mockRide);
        when(rideService.getAllRideByUserId(userToken)).thenReturn(expectedRides);

        // Act
        List<Ride> actualRides = rideController.getAllRideByUserId(userAuthHeader);

        // Assert
        assertNotNull(actualRides);
        assertEquals(1, actualRides.size());
        verify(rideService, times(1)).getAllRideByUserId(userToken);
    }

    @Test
    void getAllRideByDriverId_ShouldReturnListOfRides() {
        // Arrange
        // Generate a token for a DRIVER
        String driverToken = generateTestToken("driver@test.com", "DRIVER");
        String driverAuthHeader = "Bearer " + driverToken;

        List<Ride> expectedRides = Arrays.asList(mockRide);
        when(rideService.getAllRideByDriverId(driverToken)).thenReturn(expectedRides);

        // Act
        List<Ride> actualRides = rideController.getAllRideByDriverId(driverAuthHeader);

        // Assert
        assertNotNull(actualRides);
        assertEquals(1, actualRides.size());
        verify(rideService, times(1)).getAllRideByDriverId(driverToken);
    }


    // ... [Other tests omitted for brevity, using userAuthHeader or driverAuthHeader as needed] ...

    @Test
    void acceptRideRequest_ShouldReturnOkWithUpdatedRide() {
        // Arrange
        String driverToken = generateTestToken("driver@test.com", "DRIVER");
        String driverAuthHeader = "Bearer " + driverToken;

        Ride updatedRide = new Ride();
        updatedRide.setRiderId(1);
        updatedRide.setStatus(Status.ACCEPTED);

        when(rideService.updateRide(driverToken, mockRide)).thenReturn(updatedRide);

        // Act
        ResponseEntity<?> response = rideController.acceptRideRequest(driverAuthHeader, mockRide);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRide, response.getBody());
        verify(rideService, times(1)).updateRide(driverToken, mockRide);
    }

    @Test
    void getPendingRideOfCurrentUser_ShouldReturnRide() {
        // Arrange
        Ride pendingRide = mockRide;
        when(rideService.getPendingRideOfCurrentUser(userToken)).thenReturn(pendingRide);

        // Act
        Ride actualRide = rideController.getPendingRideOfCurrentUser(userAuthHeader);

        // Assert
        assertNotNull(actualRide);
        assertEquals(pendingRide.getRiderId(), actualRide.getRiderId());
        verify(rideService, times(1)).getPendingRideOfCurrentUser(userToken);
    }

    // ... [Add back other methods like getRideByDriverIdAndUserId, getRideStatusById, etc.] ...

}