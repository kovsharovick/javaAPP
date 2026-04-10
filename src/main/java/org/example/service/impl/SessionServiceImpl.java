package org.example.service.impl;

import org.example.model.*;
import org.example.repository.FilmRepository;
import org.example.repository.SessionRepository;
import org.example.repository.TicketRepository;
import org.example.service.SessionService;

import java.time.LocalDateTime;
import java.util.List;

public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final FilmRepository filmRepository;
    private final TicketRepository ticketRepository;

    public SessionServiceImpl(SessionRepository sessionRepository, FilmRepository filmRepository, TicketRepository ticketRepository) {
        this.sessionRepository = sessionRepository;
        this.filmRepository = filmRepository;
        this.ticketRepository = ticketRepository;
    }


    @Override
    public void createSession(Integer hallId, Integer filmId, LocalDateTime startTime) {
        Film film = filmRepository.findById(filmId);
        if (film == null) {
            throw new IllegalStateException("Фильм не найден");
        }
        LocalDateTime endTime = startTime.plus(film.getDuration());
        if (sessionRepository.existsOverlap(hallId, startTime, endTime, null)) {
            throw new IllegalStateException("Сеанс в это время уже существует!");
        }
        Session session = new Session();
        session.setFilmId(filmId);
        session.setHallId(hallId);
        session.setStartTime(startTime);
        session.setFinishTime(startTime.plus(film.getDuration()));
        sessionRepository.save(session);
    }

    @Override
    public void updateSession(Session session) {
        Session existing = sessionRepository.findById(session.getId());
        if (existing == null) throw new IllegalArgumentException("Сеанс не найден");

        List<Ticket> tickets = ticketRepository.findBySessionId(session.getId());
        boolean hasSoldTickets = tickets.stream()
                .anyMatch(t -> t.getTicketStatus() == TicketStatus.SOLD || t.getTicketStatus() == TicketStatus.RESERVED);
        if (hasSoldTickets) {
            if (!existing.getHallId().equals(session.getHallId()) ||
                    !existing.getFilmId().equals(session.getFilmId()) ||
                    !existing.getStartTime().equals(session.getStartTime())) {
                throw new IllegalStateException("Нельзя изменить зал, фильм или время сеанса, так как на него уже проданы билеты");
            }
        }
        sessionRepository.update(session);
    }

    @Override
    public void markFinishedSessions() {
        List<Session> finishedSessions = sessionRepository.findByFinishTimeBefore(LocalDateTime.now());
        for (Session session : finishedSessions) {
            ticketRepository.markTicketsAsUsedForSession(session.getId());
        }
    }

    @Override
    public boolean delete(Integer id, boolean deleteWithHistory) {
        List<Ticket> tickets = ticketRepository.findBySessionId(id);
        if (!tickets.isEmpty()) {
            if (deleteWithHistory) {
                for (Ticket ticket : tickets) {
                    if (ticket.getTicketStatus() == TicketStatus.RESERVED || ticket.getTicketStatus() == TicketStatus.SOLD) {
                        throw new IllegalStateException("Невозможно удалить сеанс, так как существуют неиспользованные билеты с ним");
                    } else {
                        ticketRepository.delete(ticket.getId()); // использованные билеты тоже удаляем.
                    }
                }
            } else {
                throw new IllegalStateException("Невозможно удалить сеанс, так как существуют неиспользованные билеты с ним");
            }
            return sessionRepository.delete(id);
        }
        return sessionRepository.delete(id);
    }

    @Override
    public List<Session> findByFilmId(Integer filmId) {
        return sessionRepository.findByFilmId(filmId);
    }

    @Override
    public List<Session> findByHallId(Integer hallId) {
        return sessionRepository.findByHallId(hallId);
    }

    @Override
    public List<Session> findByStartTimeBetween(LocalDateTime from, LocalDateTime to) {
        return sessionRepository.findByStartTimeBetween(from, to);
    }

    @Override
    public List<Session> findByFilmIdAndDateRange(Integer filmId, LocalDateTime from, LocalDateTime to) {
        return sessionRepository.findByFilmIdAndStartTimeBetween(filmId, from, to);
    }

    @Override
    public boolean existsOverlap(Integer hallId, LocalDateTime startTime, LocalDateTime endTime, Integer excludeSessionId) {
        return sessionRepository.existsOverlap(hallId, startTime, endTime, excludeSessionId);
    }

    @Override
    public List<Session> findUpcoming(LocalDateTime from) {
        return sessionRepository.findByStartTimeAfter(from);
    }

    @Override
    public Session getById(Integer id) {
        return sessionRepository.findById(id);
    }

    @Override
    public List<Session> getAll() {
        return sessionRepository.findAll();
    }
}
