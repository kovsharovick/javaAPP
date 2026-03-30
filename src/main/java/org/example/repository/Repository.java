package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    T save(T entity);

    Optional<T> findById(Integer id);

    List<T> findAll();

    void update(T entity);

    boolean delete(Integer id);
}
