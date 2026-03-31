package org.example.service;

import org.example.model.Place;

import java.util.List;

public interface PlaceService extends Service<Place, Integer> {
    List<Place> findByHallId(Integer hallId);

    Place save(Place place);

    void update(Place place);

    boolean delete(Integer id);

    // Генерация всех мест для зала (ряды, места)
    void generatePlacesForHall(Integer hallId, int rows, int seatsPerRow);

    // Получение свободных мест для конкретного сеанса
    List<Place> getFreePlacesForSession(Integer sessionId);
}