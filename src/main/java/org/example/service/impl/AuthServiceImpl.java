package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.AuthContext;
import org.example.service.AuthService;
import org.example.service.PasswordHasher;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthContext authContext;
    private final PasswordHasher passwordHasher;

    public AuthServiceImpl(UserRepository userRepository, AuthContext authContext, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.authContext = authContext;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public boolean login(String email, String password) {
        if (email == null || password == null) {
            return false;
        }
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();
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