package org.example.service.impl;

import org.example.model.Film;
import org.example.model.Session;
import org.example.repository.FilmRepository;
import org.example.repository.SessionRepository;
import org.example.service.FilmService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FilmServiceImpl implements FilmService {

    private final SessionRepository sessionRepositoy;
    private final FilmRepository filmRepository;

    public FilmServiceImpl(SessionRepository sessionRepositoy, FilmRepository filmRepository) {
        this.sessionRepositoy = sessionRepositoy;
        this.filmRepository = filmRepository;
    }


    @Override
    public Film save(Film film) {
        return filmRepository.save(film);
    }

    @Override
    public void update(Film film) {
        filmRepository.update(film);
    }

    @Override
    public boolean delete(Integer id) {
        List<Session> sessions = sessionRepositoy.findByFilmId(id);
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
        List<Session> sessions = sessionRepositoy.findAll();
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
