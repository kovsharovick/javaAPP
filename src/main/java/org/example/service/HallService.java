package org.example.service;

import org.example.model.Hall;

public interface HallService extends Service<Hall, Integer> {
    Hall save(Hall hall);

    void update(Hall hall);

    boolean delete(Integer id);
}