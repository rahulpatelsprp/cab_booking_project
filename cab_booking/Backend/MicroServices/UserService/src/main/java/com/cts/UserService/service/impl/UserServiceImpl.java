package com.cts.UserService.service.impl;

import com.cts.UserService.entity.User;
import com.cts.UserService.exception.UserAlreadyFoundException;
import com.cts.UserService.exception.UserNotFoundException;
import com.cts.UserService.repo.UserRepo;
import com.cts.UserService.securityConfig.JwtUtil;
import com.cts.UserService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
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
    public User updateUser(String token,User user) {
        User oldData=userRepo.findByEmailContainingIgnoreCase(user.getEmail()).orElseThrow(()->new UserNotFoundException("Couldn't find the user"));
        String username=jwtUtil.extractUsername(token);

        if(!oldData.getEmail().equals(username))
            throw new RuntimeException("Unauthorized access");

        if(!oldData.getEmail().equals(user.getEmail()))
            userRepo.findByEmailContainingIgnoreCase(user.getEmail())
                    .ifPresent((tempUser)->{
                        throw new UserAlreadyFoundException("An user with "+tempUser.getEmail()+" email id already exists");});

        log.info("Updating user with ID: {}", user.getUserId());
        oldData.setName(user.getName());
        oldData.setEmail(user.getEmail());
        oldData.setPhone(user.getPhone());
        if(user.getPasswordHash()!=null){
            oldData.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        log.info("User updated successfully.");
        return userRepo.save(oldData);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepo.findByEmailContainingIgnoreCase(email);
    }

    @Override
    public Optional<User> getUserByPhone(long phone) {
        log.info("Fetching user by phone number: {}", phone);
        return userRepo.findByPhone(phone);
    }
    
}