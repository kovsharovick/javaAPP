package org.example.repository;

import java.util.List;

public interface Repository<T> {
    T save(T entity);

    T findById(Integer id);

    List<T> findAll();

    void update(T entity);

    boolean delete(Integer id);
}
