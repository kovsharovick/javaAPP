package org.example.repository;

import org.example.model.Ticket;
import org.example.model.TicketStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TicketRepository extends Repository<Ticket> {
    List<Ticket> findByOrderId(Integer orderId);

    List<Ticket> findBySessionId(Integer sessionId);

    List<Ticket> findByPlaceId(Integer placeId);

    boolean deleteByOrderId(Integer orderId);

    boolean isPlaceFree(Connection conn, Integer sessionId, Integer placeId) throws SQLException;

    void saveWithConnection(Connection conn, Ticket ticket) throws SQLException;

    void updateTicketStatusByOrderId(Connection conn, Integer orderId, TicketStatus oldStatus, TicketStatus newStatus) throws SQLException;

    void markTicketsAsUsedForSession(Integer sessionId);
}