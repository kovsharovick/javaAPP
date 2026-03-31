package org.example.service;

import org.example.model.User;

import java.util.Optional;

public interface UserService extends Service<User, Integer>{
    // Регистрация нового пользователя
    User register(String name, String email, String password, boolean isAdmin);

    Optional<User> findById(Integer id);

    // Обновление профиля (имя, email, пароль)
    void updateProfile(User user, String name, String email, String password);

    // Проверка существования email
    boolean existsByEmail(String email);
}