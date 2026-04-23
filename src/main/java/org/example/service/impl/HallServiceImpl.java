package org.example.service.impl;

import org.example.model.Hall;
import org.example.model.Place;
import org.example.model.Session;
import org.example.repository.HallRepository;
import org.example.repository.PlaceRepository;
import org.example.repository.SessionRepository;
import org.example.service.AuthContext;
import org.example.service.HallService;

import java.util.List;

public class HallServiceImpl implements HallService {
    private final HallRepository hallRepository;
    private final SessionRepository sessionRepository;
    private final PlaceRepository placeRepository;
    private final AuthContext authContext;

    public HallServiceImpl(HallRepository hallRepository, SessionRepository sessionRepository, PlaceRepository placeRepository, AuthContext authContext) {
        this.hallRepository = hallRepository;
        this.sessionRepository = sessionRepository;
        this.placeRepository = placeRepository;
        this.authContext = authContext;
    }

    @Override
    public Hall save(Hall hall) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        return hallRepository.save(hall);
    }

    @Override
    public void update(Hall hall) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        hallRepository.update(hall);
    }

    @Override
    public boolean delete(Integer id) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        List<Session> sessions = sessionRepository.findByHallId(id);
        List<Place> places = placeRepository.findByHallId(id);
        if (!sessions.isEmpty()) {
            throw new IllegalStateException("Невозможно удалить зал, так как существуют сеансы в нем");
        }
        for (Place place : places) {
            placeRepository.delete(place.getId());
        }
        return hallRepository.delete(id);
    }

    @Override
    public Hall getById(Integer id) {
        return hallRepository.findById(id);
    }

    @Override
    public List<Hall> getAll() {
        return hallRepository.findAll();
    }
}
