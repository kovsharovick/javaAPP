package org.example.repository.impl;

import org.example.config.DatabaseConnection;
import org.example.model.Place;
import org.example.model.TypePlace;
import org.example.repository.PlaceRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaceRepositoryImpl implements PlaceRepository {

    @Override
    public Place save(Place place) {
        String sql = "INSERT INTO place (id_hall, rows, seat, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, place.getHallId());
            pstmt.setInt(2, place.getRows());
            pstmt.setInt(3, place.getSeat());
            pstmt.setString(4, (place.getTypePlace()).toString());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    place.setId(rs.getInt("id_place"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении места", e);
        }
        return place;
    }

    @Override
    public Place findById(Integer id) {
        String sql = "SELECT * FROM place WHERE id_place = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске места", e);
        }
        return null;
    }

    @Override
    public List<Place> findAll() {
        List<Place> places = new ArrayList<>();
        String sql = "SELECT * FROM place";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) places.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка мест", e);
        }
        return places;
    }

    @Override
    public void update(Place place) {
        String sql = "UPDATE place SET id_hall=?, rows=?, seat=?, type=? WHERE id_place=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, place.getHallId());
            pstmt.setInt(2, place.getRows());
            pstmt.setInt(3, place.getSeat());
            pstmt.setString(4, (place.getTypePlace()).toString());
            pstmt.setInt(5, place.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении места", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM place WHERE id_place = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении места", e);
        }
    }

    @Override
    public List<Place> findByHallId(Integer hallId) {
        List<Place> places = new ArrayList<>();
        String sql = "SELECT * FROM place WHERE id_hall = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, hallId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) places.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка мест по айди зала", e);
        }
        return places;
    }

    private Place mapResultSet(ResultSet rs) throws SQLException {
        return new Place(
                rs.getInt("id_place"),
                rs.getInt("id_hall"),
                rs.getInt("rows"),
                rs.getInt("seat"),
                TypePlace.valueOf(rs.getString("type"))
        );
    }
}