package org.example.service.impl;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher implements org.example.service.PasswordHasher {

    private static final int LOG_ROUNDS = 12;

    @Override
    public String hash(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Пароль не может быть null");
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    @Override
    public boolean matches(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(rawPassword, storedHash);
        } catch (Exception e) {
            return false;
        }
    }
}

