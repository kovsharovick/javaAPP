package org.example.repository;

import org.example.model.Ticket;

import java.util.List;

public interface TicketRepository extends Repository<Ticket> {
    List<Ticket> findByOrderId(Integer orderId);

    List<Ticket> findBySessionId(Integer sessionId);

    List<Ticket> findByPlaceId(Integer placeId);

    boolean deleteByOrderId(Integer orderId);
}