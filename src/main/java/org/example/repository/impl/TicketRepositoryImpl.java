package org.example.repository.impl;

import org.example.config.DataSourceProvider;
import org.example.config.DatabaseConnection;
import org.example.model.Ticket;
import org.example.model.TicketStatus;
import org.example.repository.TicketRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {

    @Override
    public Ticket save(Ticket ticket) {
        String sql = "INSERT INTO ticket (id_orders, id_place, id_session, price, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, ticket.getOrdersId());
            pstmt.setInt(2, ticket.getPlaceId());
            pstmt.setInt(3, ticket.getSessionId());
            pstmt.setBigDecimal(4, ticket.getPrice());
            pstmt.setObject(5, ticket.getTicketStatus().toString(), java.sql.Types.OTHER);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setId(rs.getInt("id_ticket"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении билета", e);
        }
        return ticket;
    }

    @Override
    public void saveWithConnection(Connection conn, Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket (id_orders, id_place, id_session, price, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, ticket.getOrdersId());
            pstmt.setInt(2, ticket.getPlaceId());
            pstmt.setInt(3, ticket.getSessionId());
            pstmt.setBigDecimal(4, ticket.getPrice());
            pstmt.setObject(5, ticket.getTicketStatus().toString(), java.sql.Types.OTHER);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setId(rs.getInt("id_ticket"));
                }
            }
        }
    }

    @Override
    public void updateTicketStatusByOrderId(Connection conn, Integer orderId, TicketStatus oldStatus, TicketStatus newStatus) throws SQLException {
        String sql = "UPDATE ticket SET status=? WHERE id_orders=? AND status=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, newStatus.toString(), java.sql.Types.OTHER);
            pstmt.setInt(2, orderId);
            pstmt.setObject(3, oldStatus.toString(), java.sql.Types.OTHER);
            pstmt.executeUpdate();
        }
    }

    @Override
    public Ticket findById(Integer id) {
        String sql = "SELECT * FROM ticket WHERE id_ticket = ?";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске билета", e);
        }
        return null;
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket";
        try (Connection conn = DataSourceProvider.getConnection();
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
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticket.getOrdersId());
            pstmt.setInt(2, ticket.getPlaceId());
            pstmt.setInt(3, ticket.getSessionId());
            pstmt.setBigDecimal(4, ticket.getPrice());
            pstmt.setObject(5, ticket.getTicketStatus().toString(), java.sql.Types.OTHER);
            pstmt.setInt(6, ticket.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении билета", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM ticket WHERE id_ticket = ?";
        try (Connection conn = DataSourceProvider.getConnection();
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
        try (Connection conn = DataSourceProvider.getConnection();
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
        try (Connection conn = DataSourceProvider.getConnection();
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
        try (Connection conn = DataSourceProvider.getConnection();
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
    public List<Ticket> findByUserId(Integer userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.* FROM ticket t JOIN orders o ON t.id_orders = o.id_orders WHERE o.id_user_data = ?";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) tickets.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка билетов по айди пользователя", e);
        }
        return tickets;
    }

    @Override
    public List<Ticket> findBySessionIdAndStatus(Integer sessionId, TicketStatus status) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE id_session = ? AND status = ?";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sessionId);
            pstmt.setObject(2, status.toString(), java.sql.Types.OTHER);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) tickets.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка билетов по айди сеанса и статусу", e);
        }
        return tickets;
    }

    @Override
    public List<Ticket> findByStatus(TicketStatus status) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE status = ?";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, status.toString(), java.sql.Types.OTHER);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) tickets.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка билетов по статусу", e);
        }
        return tickets;
    }

    @Override
    public void markTicketsAsUsedForSession(Integer sessionId) {
        String sql = "UPDATE ticket SET status = ? WHERE id_session = ? AND status = ?";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, TicketStatus.USED.toString(), java.sql.Types.OTHER);
            pstmt.setInt(2, sessionId);
            pstmt.setObject(3, TicketStatus.SOLD.toString(), java.sql.Types.OTHER);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении статуса билетов", e);
        }
    }

    @Override
    public long countSoldTicketsBySession(Integer sessionId) {
        String sql = "SELECT COUNT(*) FROM ticket WHERE id_session = ? AND status IN ('SOLD', 'RESERVED')";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sessionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при вычислении доходов", e);
        }
    }

    @Override
    public boolean deleteByOrderId(Integer orderId) {
        String sql = "DELETE FROM ticket WHERE id_orders = ?";
        try (Connection conn = DataSourceProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении билетов по айди заказа", e);
        }
    }

    @Override
    public boolean isPlaceFree(Connection conn, Integer sessionId, Integer placeId) throws SQLException {
        String sql = "SELECT 1 FROM ticket WHERE id_session = ? AND id_place = ? AND status IN ('RESERVED', 'SOLD') FOR UPDATE";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sessionId);
            pstmt.setInt(2, placeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return !rs.next();
            }
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