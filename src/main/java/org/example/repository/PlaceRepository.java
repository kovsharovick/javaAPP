package org.example.repository;

import org.example.model.Place;

import java.util.List;

public interface PlaceRepository extends Repository<Place> {
    List<Place> findByHallId(Integer hallId);
}