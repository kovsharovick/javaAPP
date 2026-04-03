package org.example.service;

import org.example.model.Place;
import org.example.repository.PlaceRepository;

import java.util.List;

public interface PlaceService extends Service<Place, Integer> {

    List<Place> findByHallId(Integer hallId);

    Place save(Place place);

    void update(Place place);

    boolean delete(Integer id, boolean deleteWithHistory);

    //генерация всех мест для зала (ряды, места).
    void generatePlacesForHall(Integer hallId);

    //получение свободных мест для конкретного сеанса.
    List<Place> getFreePlacesForSession(Integer sessionId);
}