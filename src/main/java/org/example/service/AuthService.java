package org.example.service;

import org.example.model.User;

public interface AuthService {
    boolean login(String email, String password);

    void logout();

    User getCurrentUser();
}