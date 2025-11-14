package com.cts.cab_booking.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.helper.AuthRequest;
import com.cts.cab_booking.helper.Role;
import com.cts.cab_booking.securityConfig.JwtUtil;
import com.cts.cab_booking.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private User passengerUser;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        // Mock User object
        passengerUser = new User();
        passengerUser.setUserId(1);
        passengerUser.setEmail("test@example.com");
        passengerUser.setPasswordHash("encodedPassword");
        passengerUser.setRole(Role.USER);

        // Mock AuthRequest
        authRequest = new AuthRequest("test@example.com", "rawPassword");
    }

    // --- saveUser (/register) Tests ---

    @Test
    @DisplayName("POST /register - Successful user registration")
    void saveUser_Success() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("newuser@test.com");
        newUser.setPasswordHash("raw");

        // User not found scenario
        when(userService.getUserByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = userController.saveUser(newUser);

        // Assert
        assertEquals("SavedUser", response.getBody());
        assertEquals(Role.USER, newUser.getRole(), "Role must be set to USER");
        // Verify service save method was called
        verify(userService, times(1)).saveUser(newUser);
    }

    @Test
    @DisplayName("POST /register - User already exists returns 'AlreadyFound'")
    void saveUser_AlreadyExists_ReturnsAlreadyFound() {
        // Arrange
        when(userService.getUserByEmail(passengerUser.getEmail())).thenReturn(Optional.of(passengerUser));

        // Act
        ResponseEntity<String> response = userController.saveUser(passengerUser);

        // Assert
        assertEquals("AlreadyFound", response.getBody());
        // Verify save method was NOT called
        verify(userService, never()).saveUser(any(User.class));
    }

    // --- getUserById (/profile/{id}) Tests ---

    @Test
    @DisplayName("GET /profile/{id} - Successfully retrieve user by ID")
    void getUserById_Success() {
        // Arrange
        when(userService.getUserById(1)).thenReturn(passengerUser);

        // Act
        ResponseEntity<User> response = userController.getUserById(1);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(passengerUser, response.getBody());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    @DisplayName("GET /profile/{id} - Should throw NoSuchElementException when user not found")
    void getUserById_NotFound_ThrowsException() {
        // Arrange
        when(userService.getUserById(99)).thenThrow(new java.util.NoSuchElementException("User not found"));

        // Act & Assert
        assertThrows(java.util.NoSuchElementException.class, () -> userController.getUserById(99));
    }

    // --- login (/login) Tests ---



    @Test
    @DisplayName("POST /login - Fails on Bad Credentials")
    void login_BadCredentials_ThrowsException() {
        // Arrange
        // Mock AuthenticationManager failure
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> userController.login(authRequest));

        // FIX: Verify no further calls were made, expecting TWO arguments
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
        verify(userService, never()).getUserByEmail(anyString());
    }



    // --- delete (/delete/{id}) Tests ---

    @Test
    @DisplayName("DELETE /delete/{id} - Successful user deletion")
    void delete_Success() {
        // Arrange
        when(userService.deleteUserById(1)).thenReturn(passengerUser);

        // Act
        User result = userController.delete(1);

        // Assert
        assertNotNull(result);
        assertEquals(passengerUser.getUserId(), result.getUserId());
        verify(userService, times(1)).deleteUserById(1);
    }

    @Test
    @DisplayName("DELETE /delete/{id} - User not found returns null and logs info")
    void delete_UserNotFound_ReturnsNull() {
        // Arrange
        when(userService.deleteUserById(99)).thenReturn(null);

        // Act
        User result = userController.delete(99);

        // Assert
        assertNull(result);
        verify(userService, times(1)).deleteUserById(99);
    }

    // --- allUserDetails (GET) Tests ---

    @Test
    @DisplayName("GET / - Retrieve all user details successfully")
    void allUserDetails_Success() {
        // Arrange
        List<User> userList = Arrays.asList(passengerUser, new User());
        when(userService.getAllUser()).thenReturn(userList);

        // Act
        ResponseEntity<?> response = userController.allUserDetails();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(userList, response.getBody());
        verify(userService, times(1)).getAllUser();
    }
}