package org.example.service.impl;

import org.example.model.*;
import org.example.repository.HallRepository;
import org.example.repository.PlaceRepository;
import org.example.repository.SessionRepository;
import org.example.repository.TicketRepository;
import org.example.service.PlaceService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final HallRepository hallRepository;
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository, HallRepository hallRepository, TicketRepository ticketRepository, SessionRepository sessionRepository) {
        this.placeRepository = placeRepository;
        this.hallRepository = hallRepository;
        this.ticketRepository = ticketRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Place> findByHallId(Integer hallId) {
        return placeRepository.findByHallId(hallId);
    }

    @Override
    public Place save(Place place) {
        return placeRepository.save(place);
    }

    @Override
    public void update(Place place) {
        placeRepository.update(place);
    }

    @Override
    public boolean delete(Integer id, boolean deleteWithHistory) {
        List<Ticket> tickets = ticketRepository.findByPlaceId(id);
        if (!tickets.isEmpty()) {
            if (deleteWithHistory) {
                for (Ticket ticket : tickets) {
                    if (ticket.getTicketStatus() == TicketStatus.RESERVED || ticket.getTicketStatus() == TicketStatus.SOLD) {
                        throw new IllegalStateException("Невозможно удалить место, так как существуют неиспользованные билеты с ним");
                    } else {
                        ticketRepository.delete(ticket.getId()); // использованные билеты тоже удаляем.
                    }
                }
            } else {
                //ставим номер ряда и места равные 0, это служит меткой - его больше нет.
                Place place = placeRepository.findById(id);
                place.setSeat(0);
                place.setRows(0);
                placeRepository.update(place);
                return true;
            }
            return placeRepository.delete(id);
        }
        return placeRepository.delete(id);
    }


    @Override
    public void generatePlacesForHall(Integer hallId) {
        Hall hall = hallRepository.findById(hallId);
        if (hall == null) throw new IllegalArgumentException("Зал не найден");
        List<Place> existing = placeRepository.findByHallId(hallId);
        for (Place p : existing) placeRepository.delete(p.getId());
        int rows = hall.getRows();
        int seats = hall.getSeatsPerRow();
        for (int row = 1; row <= rows; row++) {
            for (int seat = 1; seat <= seats; seat++) {
                Place place = new Place();
                place.setHallId(hallId);
                place.setRows(row);
                place.setSeat(seat);
                if (row == rows) {
                    place.setTypePlace(TypePlace.VIP);
                } else {
                    place.setTypePlace(TypePlace.STANDARD);
                }
                placeRepository.save(place);
            }
        }
    }

    @Override
    public List<Place> getFreePlacesForSession(Integer sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Сеанс с id " + sessionId + " не найден");
        }
        Integer hallId = session.getHallId();
        List<Place> allPlaces = placeRepository.findByHallId(hallId);

        List<Ticket> tickets = ticketRepository.findBySessionId(sessionId);
        Set<Integer> takenPlaceIds = tickets.stream()
                .filter(t -> t.getTicketStatus() == TicketStatus.RESERVED || t.getTicketStatus() == TicketStatus.SOLD)
                .map(Ticket::getPlaceId)
                .collect(Collectors.toSet());
        return allPlaces.stream()
                .filter(place -> !takenPlaceIds.contains(place.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Place getById(Integer id) {
        return placeRepository.findById(id);
    }

    @Override
    public List<Place> getAll() {
        return placeRepository.findAll();
    }
}
