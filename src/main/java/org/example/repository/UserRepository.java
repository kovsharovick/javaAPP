package org.example.repository;

import org.example.model.User;

import java.util.Optional;

public interface UserRepository extends Repository<User> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}