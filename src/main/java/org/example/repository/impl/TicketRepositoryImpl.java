package org.example.repository.impl;

import org.example.config.DatabaseConnection;
import org.example.model.Order;
import org.example.model.Ticket;
import org.example.model.TicketStatus;
import org.example.repository.TicketRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepositoryImpl implements TicketRepository {

    @Override
    public Ticket save(Ticket ticket) {
        String sql = "INSERT INTO ticket (id_orders, id_place, id_session, price, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, ticket.getOrdersId());
            pstmt.setInt(2, ticket.getPlaceId());
            pstmt.setInt(3, ticket.getSessionId());
            pstmt.setBigDecimal(4, ticket.getPrice());
            pstmt.setString(5, (ticket.getTicketStatus()).toString());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) ticket.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении билета", e);
        }
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(Integer id) {
        String sql = "SELECT * FROM ticket WHERE id_ticket = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске билета", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) tickets.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка билетов", e);
        }
        return tickets;
    }

    @Override
    public void update(Ticket ticket) {
        String sql = "UPDATE ticket SET id_orders=?, id_place=?, id_session=?, price=?, status=? WHERE id_ticket=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticket.getOrdersId());
            pstmt.setInt(2, ticket.getPlaceId());
            pstmt.setInt(3, ticket.getSessionId());
            pstmt.setBigDecimal(4, ticket.getPrice());
            pstmt.setString(5, (ticket.getTicketStatus()).toString());
            pstmt.setInt(6, ticket.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении билета", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM ticket WHERE id_ticket = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении билета", e);
        }
    }

    @Override
    public List<Ticket> findByOrderId(Integer orderId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE id_orders = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) tickets.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка билетов по айди заказа", e);
        }
        return tickets;
    }

    @Override
    public List<Ticket> findBySessionId(Integer sessionId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE id_session = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sessionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) tickets.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка билетов по айди сеанса", e);
        }
        return tickets;
    }

    @Override
    public List<Ticket> findByPlaceId(Integer placeId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE id_place = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, placeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) tickets.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка билетов по айди места", e);
        }
        return tickets;
    }

    @Override
    public boolean deleteByOrderId(Integer orderId) {
        String sql = "DELETE FROM ticket WHERE id_orders = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении билетов по айди заказа", e);
        }
    }

    private Ticket mapResultSet(ResultSet rs) throws SQLException {
        return new Ticket(
                rs.getInt("id_ticket"),
                rs.getInt("id_orders"),
                rs.getInt("id_place"),
                rs.getInt("id_session"),
                rs.getBigDecimal("price"),
                TicketStatus.valueOf(rs.getString("status"))
        );
    }
}