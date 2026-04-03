package org.example.service;

import org.example.model.User;

import java.util.Optional;

public interface UserService extends Service<User, Integer>{
    User register(String name, String email, String password, boolean isAdmin);

    User findById(Integer id);

    void updateProfile(User user, String name, String email, String password);

    boolean existsByEmail(String email);
}