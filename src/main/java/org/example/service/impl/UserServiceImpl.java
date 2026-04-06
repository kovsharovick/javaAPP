package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.service.PasswordHasher;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserServiceImpl(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User register(String name, String email, String password, boolean isAdmin) {
        if (email != null && !isValidEmail(email)) {
            throw new IllegalArgumentException("Неверный формат email");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordHasher.hash(password));
        user.setAdmin(isAdmin);
        return userRepository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateProfile(User currentUser, String name, String email, String password) {
        if (currentUser == null) {
            throw new IllegalArgumentException("Пользователь не авторизован");
        }
        if (email != null && !isValidEmail(email)) {
            throw new IllegalArgumentException("Неверный формат email");
        }
        if (email != null && !email.equals(currentUser.getEmail()) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email уже используется другим пользователем");
        }
        User updated = new User();
        updated.setId(currentUser.getId());
        updated.setName(name != null ? name : currentUser.getName());
        updated.setEmail(email != null ? email : currentUser.getEmail());

        updated.setPassword(password != null ? passwordHasher.hash(password) : currentUser.getPassword());
        updated.setAdmin(currentUser.getAdmin());
        userRepository.update(updated);

        currentUser.setName(updated.getName());
        currentUser.setEmail(updated.getEmail());
        currentUser.setPassword(updated.getPassword());
    }

    @Override
    public void updateStatus(User user, boolean isAdmin) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        user.setAdmin(isAdmin);
        userRepository.update(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    private boolean isValidEmail(String email) {
        try {
            new InternetAddress(email).validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
}