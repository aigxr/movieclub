package com.example.movieclub.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findUserById(Long id);
    Optional<User> findUserByUsername(String username);
    List<User> findUserByUsernameContainingIgnoreCase(String keyword);
    void deleteByUsername(String username);
    void deleteByEmail(String email);
}
