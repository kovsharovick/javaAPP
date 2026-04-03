package org.example.service;

import org.example.model.Film;

import java.util.List;

public interface FilmService extends Service<Film, Integer> {
    Film save(Film film);

    void update(Film film);

    boolean delete(Integer id);

    //фильмы по названию.
    List<Film> findByNameContaining(String namePart);

    //все фильмы у которых есть будущие сеансы.
    List<Film> findUsingFilm();
}