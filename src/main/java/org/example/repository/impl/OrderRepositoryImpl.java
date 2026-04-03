package org.example.repository.impl;

import org.example.config.DatabaseConnection;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.repository.OrderRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public Order save(Order order) {
        String sql = "INSERT INTO orders (id_user_data, amount, date_and_time, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, order.getUserId());
            pstmt.setBigDecimal(2, order.getAmount());
            pstmt.setObject(3, order.getDateTime());
            pstmt.setString(4, (order.getOrderStatus()).toString());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) order.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении заказа", e);
        }
        return order;
    }

    @Override
    public Order createOrderWithConnection(Connection conn, Integer userId) throws SQLException {
        String sql = "INSERT INTO orders (id_user_data, amount, date_and_time, status, reserved_until) VALUES (?, ?, ?, ?, ?)";
        Order order = new Order();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.setBigDecimal(2, BigDecimal.ZERO);
            pstmt.setObject(3, LocalDateTime.now());
            pstmt.setString(4, OrderStatus.WAIT_PAYMENT.toString());
            pstmt.setObject(5, LocalDateTime.now().plusMinutes(15));

            pstmt.executeUpdate();

            order.setUserId(userId);
            order.setAmount(BigDecimal.ZERO);
            order.setDateTime(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.WAIT_PAYMENT);
            order.setReservedUntil(LocalDateTime.now().plusMinutes(15));

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getInt(1));
                }
            }
        }
        return order;
    }

    @Override
    public Order findById(Integer id) {
        String sql = "SELECT * FROM orders WHERE id_orders = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске заказа", e);
        }
        return null;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) orders.add(mapResultSet(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка заказов", e);
        }
        return orders;
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET id_user_data=?, amount=?, date_and_time=?, status=? WHERE id_orders=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order.getUserId());
            pstmt.setBigDecimal(2, order.getAmount());
            pstmt.setObject(3, order.getDateTime());
            pstmt.setString(4, (order.getOrderStatus()).toString());
            pstmt.setInt(5, order.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении заказа", e);
        }
    }

    @Override
    public void updateWithConnection(Connection conn, Order order, LocalDateTime reservedUntil) throws SQLException {
        String sql = "UPDATE orders SET status=?, reserved_until=? WHERE id_orders=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getOrderStatus().toString());
            pstmt.setObject(2, reservedUntil);
            pstmt.setInt(3, order.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM orders WHERE id_orders = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении заказа", e);
        }
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE id_user_data = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) orders.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка заказов по айди пользователя", e);
        }
        return orders;
    }

    private Order mapResultSet(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("id_orders"),
                rs.getInt("id_user_data"),
                rs.getBigDecimal("amount"),
                rs.getObject("date_and_time", LocalDateTime.class),
                OrderStatus.valueOf(rs.getString("status")),
                rs.getObject("reserved_until", LocalDateTime.class)
        );
    }


}