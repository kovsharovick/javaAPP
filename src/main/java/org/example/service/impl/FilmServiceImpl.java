package org.example.service.impl;

import org.example.model.Film;
import org.example.model.Session;
import org.example.model.Ticket;
import org.example.model.TicketStatus;
import org.example.repository.FilmRepository;
import org.example.repository.SessionRepository;
import org.example.repository.TicketRepository;
import org.example.service.AuthContext;
import org.example.service.FilmService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FilmServiceImpl implements FilmService {

    private final SessionRepository sessionRepository;
    private final FilmRepository filmRepository;
    private final TicketRepository ticketRepository;
    private final AuthContext authContext;

    public FilmServiceImpl(SessionRepository sessionRepository, FilmRepository filmRepository, TicketRepository ticketRepository, AuthContext authContext) {
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
        this.filmRepository = filmRepository;
        this.authContext = authContext;
    }


    @Override
    public Film save(Film film) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        return filmRepository.save(film);
    }

    @Override
    public void update(Film film) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        Film existing = filmRepository.findById(film.getId());
        if (existing == null) throw new IllegalArgumentException("Фильм не найден");

        if (!existing.getDuration().equals(film.getDuration())) {
            List<Session> sessions = sessionRepository.findByFilmId(film.getId());
            for (Session s : sessions) {
                List<Ticket> tickets = ticketRepository.findBySessionId(s.getId());
                if (tickets.stream().anyMatch(t -> t.getTicketStatus() == TicketStatus.SOLD || t.getTicketStatus() == TicketStatus.RESERVED)) {
                    throw new IllegalStateException("Нельзя изменить длительность фильма, так как есть сеансы с купленными билетами");
                }
            }
        }
        filmRepository.update(film);
    }

    @Override
    public boolean delete(Integer id) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        List<Session> sessions = sessionRepository.findByFilmId(id);
        if (!sessions.isEmpty()) {
            throw new IllegalStateException("Невозможно удалить фильм, так как существуют сеансы с ним");
        }
        return filmRepository.delete(id);
    }

    @Override
    public List<Film> findByNameContaining(String namePart) {
        return filmRepository.findByNameContaining(namePart);
    }

    @Override
    public List<Film> findUsingFilm() {
        List<Session> sessions = sessionRepository.findAll();
        Set<Integer> filmId = sessions.stream()
                .filter(s -> s.getStartTime().isAfter(LocalDate.now().atStartOfDay()))
                .map(Session::getFilmId)
                .collect(Collectors.toSet());
        List<Film> films = filmRepository.findAll();
        return films.stream()
                .filter(film -> filmId.contains(film.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Film getById(Integer id) {
        return filmRepository.findById(id);
    }

    @Override
    public List<Film> getAll() {
        return filmRepository.findAll();
    }
}
