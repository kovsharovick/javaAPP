package org.example.repository.impl;

import org.example.config.DatabaseConnection;
import org.example.model.Order;
import org.example.model.Session;
import org.example.repository.SessionRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionRepositoryImpl implements SessionRepository {

    @Override
    public Session save(Session session) {
        String sql = "INSERT INTO session (id_hall, id_film, starting, finishing) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, session.getHallId());
            pstmt.setInt(2, session.getFilmId());
            pstmt.setObject(3, session.getStartTime());
            pstmt.setObject(4, session.getFinishTime());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    session.setId(rs.getInt("id_session"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении сеанса", e);
        }
        return session;
    }

    @Override
    public Session findById(Integer id) {
        String sql = "SELECT * FROM session WHERE id_session = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сеанса", e);
        }
        return null;
    }

    @Override
    public List<Session> findAll() {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) sessions.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка сеансов", e);
        }
        return sessions;
    }

    @Override
    public void update(Session session) {
        String sql = "UPDATE session SET id_hall=?, id_film=?, starting=?, finishing=? WHERE id_session=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, session.getHallId());
            pstmt.setInt(2, session.getFilmId());
            pstmt.setObject(3, session.getStartTime());
            pstmt.setObject(4, session.getFinishTime());
            pstmt.setInt(5, session.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении сеанса", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM session WHERE id_session = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении сеанса", e);
        }
    }

    @Override
    public List<Session> findByFilmId(Integer filmId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE id_film = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, filmId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) sessions.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка сеансов по айди фильма", e);
        }
        return sessions;
    }

    @Override
    public List<Session> findByHallId(Integer hallId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE id_hall = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, hallId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) sessions.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка сеансов по айди зала", e);
        }
        return sessions;
    }

    @Override
    public List<Session> findByStartTimeBetween(LocalDateTime from, LocalDateTime to) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE starting BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, from);
            pstmt.setObject(2, to);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сеансов по временному интервалу", e);
        }
        return sessions;
    }

    @Override
    public List<Session> findByFilmIdAndStartTimeBetween(Integer filmId, LocalDateTime from, LocalDateTime to) {
        String sql = "SELECT * FROM session WHERE id_film = ? AND starting BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, filmId);
            pstmt.setObject(2, from);
            pstmt.setObject(3, to);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Session> sessions = new ArrayList<>();
                while (rs.next()) sessions.add(mapResultSet(rs));
                return sessions;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сеансов по фильму и интервалу", e);
        }
    }

    @Override
    public boolean existsOverlap(Integer hallId, LocalDateTime startTime, LocalDateTime endTime, Integer excludeSessionId) {
        String sql = "SELECT COUNT(*) FROM session WHERE id_hall = ? AND starting < ? AND finishing > ?";
        if (excludeSessionId != null) {
            sql += " AND id_session != ?";
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, hallId);
            pstmt.setObject(2, endTime);
            pstmt.setObject(3, startTime);
            if (excludeSessionId != null) {
                pstmt.setInt(4, excludeSessionId);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке пересечения сеансов", e);
        }
        return false;
    }

    @Override
    public List<Session> findByStartTimeAfter(LocalDateTime time) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE starting > ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, time);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сеансов по временному интервалу", e);
        }
        return sessions;
    }

    @Override
    public List<Session> findByFinishTimeBefore(LocalDateTime now) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE finishing < ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, now);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске сеансов по временному интервалу", e);
        }
        return sessions;
    }

    private Session mapResultSet(ResultSet rs) throws SQLException {
        return new Session(
                rs.getInt("id_session"),
                rs.getInt("id_hall"),
                rs.getInt("id_film"),
                rs.getObject("starting", LocalDateTime.class),
                rs.getObject("finishing", LocalDateTime.class)
        );
    }
}