package org.example.repository;

import org.example.model.Film;

import java.util.List;

public interface FilmRepository extends Repository<Film> {
    List<Film> findByNameContaining(String namePart);
}