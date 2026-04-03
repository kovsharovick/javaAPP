package org.example.repository.impl;

import org.example.config.DatabaseConnection;
import org.example.model.Film;
import org.example.repository.FilmRepository;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilmRepositoryImpl implements FilmRepository {

    @Override
    public Film save(Film film) {
        String sql = "INSERT INTO film (name, duration, price, description, poster_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, film.getName());
            pstmt.setObject(2, film.getDuration());
            pstmt.setBigDecimal(3, film.getPrice());
            pstmt.setString(4, film.getDescription());
            pstmt.setString(5, film.getPosterUrl());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) film.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении фильма", e);
        }
        return film;
    }

    @Override
    public Film findById(Integer id) {
        String sql = "SELECT * FROM film WHERE id_film = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске фильма", e);
        }
        return null;
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) films.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка фильмов", e);
        }
        return films;
    }

    @Override
    public void update(Film film) {
        String sql = "UPDATE film SET name=?, duration=?, price=?, description=?, poster_url=? WHERE id_film=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, film.getName());
            pstmt.setObject(2, film.getDuration());
            pstmt.setBigDecimal(3, film.getPrice());
            pstmt.setString(4, film.getDescription());
            pstmt.setString(5, film.getPosterUrl());
            pstmt.setInt(6, film.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении фильма", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM film WHERE id_film = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении фильма", e);
        }
    }

    @Override
    public List<Film> findByNameContaining(String namePart) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film WHERE name ILIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + namePart + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) films.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске фильмов по имени", e);
        }
        return films;
    }

    private Film mapResultSet(ResultSet rs) throws SQLException {
        return new Film(
                rs.getInt("id_film"),
                rs.getString("name"),
                rs.getObject("duration", Duration.class),
                rs.getBigDecimal("price"),
                rs.getString("description"),
                rs.getString("poster_url")
        );
    }
}