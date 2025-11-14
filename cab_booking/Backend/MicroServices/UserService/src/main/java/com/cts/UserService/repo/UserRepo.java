package com.cts.UserService.repo;

import com.cts.UserService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmailContainingIgnoreCase(String email);

    Optional<User> findByPhone(long phone);


}
