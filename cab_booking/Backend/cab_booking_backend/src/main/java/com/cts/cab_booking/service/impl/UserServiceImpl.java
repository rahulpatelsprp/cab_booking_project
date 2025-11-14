package com.cts.cab_booking.service.impl;

import com.cts.cab_booking.entity.User;
import com.cts.cab_booking.exception.UnauthorizedAccessException;
import com.cts.cab_booking.exception.UserAlreadyFoundException;
import com.cts.cab_booking.exception.UserNotFoundException;
import com.cts.cab_booking.repository.UserRepo;
import com.cts.cab_booking.securityConfig.JwtUtil;
import com.cts.cab_booking.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;


    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User saveUser(User user) {
        User savedUser = null;
        try {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            savedUser = userRepo.save(user);
            log.info("User saved successfully with email: {}", savedUser.getEmail());
        } catch (Exception ex) {
            log.error("Failed to save user with email: {}. Error: {}", user.getEmail(), ex.getMessage());
        }
        return savedUser;
    }

    @Override
    public List<User> getAllUser() {
        log.info("Fetching all users.");
        return userRepo.findAll();
    }

    @Override
    public User getUserById(int id) {
        log.info("Fetching user by ID: {}", id);
        return userRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    return new RuntimeException("User not found");
                });
    }

    @Override
    public User deleteUserById(int id) {
        log.info("Attempting to delete user with ID: {}", id);
        Optional<User> user = userRepo.findById(id);
        if (user.isEmpty()) {
            log.warn("User not found for deletion with ID: {}", id);
            return null;
        }
        userRepo.delete(user.get());
        log.info("User deleted successfully with ID: {}", id);
        return user.get();
    }

    @Override
    public User updateUser(String token, User user) {
        //If the user not found
        User oldData = userRepo.findById(user.getUserId()).orElseThrow(() -> new UserNotFoundException("Couldn't find the user"));
        String username = jwtUtil.extractUsername(token);

        //make sure that the user is trying to his details only
        if (!oldData.getEmail().equals(username))
            throw new UnauthorizedAccessException("Unauthorized access");

        //make sure that new email id does not already exist
        if (!oldData.getEmail().equals(user.getEmail()))
            userRepo.findByEmail(user.getEmail())
                    .ifPresent((tempUser) -> {
                        throw new UserAlreadyFoundException("An user with " + tempUser.getEmail() + " email id already exists");
                    });

        log.info("Updating user with ID: {}", user.getUserId());
        oldData.setName(user.getName());
        oldData.setEmail(user.getEmail());
        oldData.setPhone(user.getPhone());
        if (user.getPasswordHash() != null) {
            oldData.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        log.info("User updated successfully.");
        return userRepo.save(oldData);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepo.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByPhone(long phone) {
        log.info("Fetching user by phone number: {}", phone);
        return userRepo.findByPhone(phone);
    }

    @Override
    public User getUserByToken(String token) {
        String email = jwtUtil.extractUsername(token);
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
    }

}