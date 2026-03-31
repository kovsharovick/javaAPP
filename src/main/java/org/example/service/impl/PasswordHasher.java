package org.example.service.impl;

import java.util.Objects;

public class PasswordHasher implements org.example.service.PasswordHasher {

    @Override
    public String hash(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String storedHash) {
        return Objects.equals(rawPassword, storedHash);
    }
}

