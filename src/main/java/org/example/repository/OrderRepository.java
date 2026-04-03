package org.example.repository;

import org.example.model.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends Repository<Order> {
    List<Order> findByUserId(Integer userId);
    Order createOrderWithConnection(Connection conn, Integer userId) throws SQLException;
    void updateWithConnection(Connection conn, Order order, LocalDateTime reservedUntil) throws SQLException;

}