package org.example.service;

import java.util.List;

public interface Service<T, Integer> {
    T getById(int id);
    List<T> getAll();

    //какие методы должны реализовывать сервисы придумаете сами
}
