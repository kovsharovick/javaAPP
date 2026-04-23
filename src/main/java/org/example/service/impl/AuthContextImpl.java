package org.example.service.impl;

import org.example.model.User;
import org.example.service.AuthContext;

public class AuthContextImpl implements AuthContext {
    private User currentUser;

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void clear() {
        this.currentUser = null;
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    @Override
    public boolean isAdmin() {
        return currentUser != null && currentUser.getAdmin();
    }
}