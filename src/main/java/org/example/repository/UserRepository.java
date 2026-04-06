package org.example.repository;

import org.example.model.User;

public interface UserRepository extends Repository<User> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}