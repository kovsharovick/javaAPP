package org.example.repository;

import java.util.List;

public interface Repository<T, Integer> {
    void add(T object);
    void removeAll();
    void update(int id, T newObject);
    T getById(int id);
    List<T> getAll();
}
