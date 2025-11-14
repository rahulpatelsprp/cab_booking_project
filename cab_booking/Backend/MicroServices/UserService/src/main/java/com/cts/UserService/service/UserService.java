package com.cts.UserService.service;


import com.cts.UserService.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface UserService {
    abstract User saveUser(User user);

    abstract List<User> getAllUser();

    abstract User getUserById(int id);

    abstract User deleteUserById(int id);

    abstract User updateUser(String token, User user);


    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByPhone(long phone);

    
}
