package com.cts.cab_booking.service.impl;

import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.helper.Role;
import com.cts.cab_booking.repository.UserRepo;
import com.cts.cab_booking.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .userId(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone(1234567890L)
                .role(Role.USER)
                .passwordHash("rawPassword1")
                .build();

        user2 = User.builder()
                .userId(2)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phone(9876543210L)
                .role(Role.DRIVER)
                .passwordHash("rawPassword2")
                .build();
    }

    @Test
    void saveUser_ShouldEncodePasswordAndSaveUser() {
        String rawPassword = user1.getPasswordHash();
        String encodedPassword = "encodedPassword1";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        userServiceImpl.saveUser(user1);

        verify(passwordEncoder, times(1)).encode(rawPassword);

        assertEquals(encodedPassword, user1.getPasswordHash());

        verify(userRepo, times(1)).save(user1);
    }

    @Test
    void saveUser_ShouldCatchExceptionAndLog_WhenRepoFails() {
        String rawPassword = user1.getPasswordHash();
        String encodedPassword = "encodedPassword1";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        doThrow(new RuntimeException("DB error")).when(userRepo).save(any(User.class));

        assertDoesNotThrow(() -> userServiceImpl.saveUser(user1));

        verify(userRepo, times(1)).save(user1);
    }

    @Test
    void getAllUser_ShouldReturnListOfAllUsers() {
        List<User> users = Arrays.asList(user1, user2);
        when(userRepo.findAll()).thenReturn(users);

        List<User> result = userServiceImpl.getAllUser();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1.getUserId(), result.get(0).getUserId());
        assertEquals(user2.getUserId(), result.get(1).getUserId());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenFound() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user1));

        User foundUser = userServiceImpl.getUserById(1);

        assertNotNull(foundUser);
        assertEquals(user1.getUserId(), foundUser.getUserId());
        assertEquals(user1.getEmail(), foundUser.getEmail());
        verify(userRepo, times(1)).findById(1);
    }


    @Test
    void deleteUserById_ShouldDeleteAndReturnUser_WhenFound() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user1));
        doNothing().when(userRepo).delete(user1);

        User deletedUser = userServiceImpl.deleteUserById(1);

        assertNotNull(deletedUser);
        assertEquals(user1.getUserId(), deletedUser.getUserId());
        verify(userRepo, times(1)).findById(1);
        verify(userRepo, times(1)).delete(user1);
    }

    @Test
    void deleteUserById_ShouldReturnNull_WhenNotFound() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());

        User deletedUser = userServiceImpl.deleteUserById(99);

        assertNull(deletedUser);
        verify(userRepo, times(1)).findById(99);
        verify(userRepo, never()).delete(any(User.class));
    }


    @Test
    void getUserByEmail_ShouldReturnOptionalUser_WhenFound() {
        when(userRepo.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user1));

        Optional<User> result = userServiceImpl.getUserByEmail("john.doe@example.com");

        assertTrue(result.isPresent());
        assertEquals(user1.getEmail(), result.get().getEmail());
        verify(userRepo, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void getUserByEmail_ShouldReturnEmptyOptional_WhenNotFound() {
        when(userRepo.findByEmail("non.existent@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userServiceImpl.getUserByEmail("non.existent@example.com");

        assertFalse(result.isPresent());
        verify(userRepo, times(1)).findByEmail("non.existent@example.com");
    }

    @Test
    void getUserByPhone_ShouldReturnOptionalUser_WhenFound() {
        when(userRepo.findByPhone(1234567890L)).thenReturn(Optional.of(user1));

        Optional<User> result = userServiceImpl.getUserByPhone(1234567890L);

        assertTrue(result.isPresent());
        assertEquals(user1.getPhone(), result.get().getPhone());
        verify(userRepo, times(1)).findByPhone(1234567890L);
    }

    @Test
    void getUserByPhone_ShouldReturnEmptyOptional_WhenNotFound() {
        when(userRepo.findByPhone(1111111111L)).thenReturn(Optional.empty());

        Optional<User> result = userServiceImpl.getUserByPhone(1111111111L);

        assertFalse(result.isPresent());
        verify(userRepo, times(1)).findByPhone(1111111111L);
    }
}