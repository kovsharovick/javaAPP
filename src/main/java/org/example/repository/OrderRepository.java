package org.example.repository;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.TicketStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends Repository<Order> {
    List<Order> findByUserId(Integer userId);

    Order createOrderWithConnection(Connection conn, Integer userId, Integer reservedTime) throws SQLException;

    void updateWithConnection(Connection conn, Order order) throws SQLException;

    void cancelOrderWithConnection(Connection conn, Integer orderId) throws SQLException;

    void confirmPaymentWithConnection(Connection conn, Integer orderId) throws SQLException;

    List<Order> findExpiredReservations();
}