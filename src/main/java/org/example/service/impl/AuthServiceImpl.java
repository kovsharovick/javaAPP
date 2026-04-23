package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.AuthContext;
import org.example.service.AuthService;
import org.example.service.PasswordHasher;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuthContext authContext;

    public AuthServiceImpl(UserRepository userRepository, AuthContext authContext, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.authContext = authContext;
    }

    @Override
    public boolean login(String email, String password) {
        if (email == null || password == null) {
            return false;
        }
        User user = userRepository.findByEmail(email);
        if (user == null) return false;
        if (passwordHasher.matches(password, user.getPassword())) {
            authContext.setCurrentUser(user);
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        authContext.clear();
    }

    @Override
    public User getCurrentUser() {
        return authContext.getCurrentUser();
    }
}