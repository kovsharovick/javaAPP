package org.example.repository.impl;

import org.example.config.DatabaseConnection;
import org.example.model.Hall;
import org.example.repository.HallRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HallRepositoryImpl implements HallRepository {

    @Override
    public Hall save(Hall hall) {
        String sql = "INSERT INTO hall (rows, seat, price) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, hall.getRows());
            pstmt.setInt(2, hall.getSeatsPerRow());
            pstmt.setBigDecimal(3, hall.getPrice());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) hall.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении зала", e);
        }
        return hall;
    }

    @Override
    public Hall findById(Integer id) {
        String sql = "SELECT * FROM hall WHERE id_hall = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске зала", e);
        }
        return null;
    }

    @Override
    public List<Hall> findAll() {
        List<Hall> halls = new ArrayList<>();
        String sql = "SELECT * FROM hall";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) halls.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка залов", e);
        }
        return halls;
    }

    @Override
    public void update(Hall hall) {
        String sql = "UPDATE hall SET rows=?, seat=?, price=? WHERE id_hall=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, hall.getRows());
            pstmt.setInt(2, hall.getSeatsPerRow());
            pstmt.setBigDecimal(3, hall.getPrice());
            pstmt.setInt(4, hall.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении зала", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM hall WHERE id_hall = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении зала", e);
        }
    }

    private Hall mapResultSet(ResultSet rs) throws SQLException {
        return new Hall(
                rs.getInt("id_hall"),
                rs.getInt("rows"),
                rs.getInt("seat"),
                rs.getBigDecimal("price")
        );
    }
}