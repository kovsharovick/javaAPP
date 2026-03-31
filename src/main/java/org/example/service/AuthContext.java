package org.example.service;

import org.example.model.User;

public interface AuthContext {
    User getCurrentUser();

    void setCurrentUser(User user);

    void clear();

    boolean isAuthenticated();
}