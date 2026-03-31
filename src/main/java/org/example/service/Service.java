package org.example.service;

import java.util.List;

public interface Service<T, Integer> {
    T getById(Integer id);

    List<T> getAll();
}
