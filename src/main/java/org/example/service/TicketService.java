package org.example.service;

import org.example.model.*;

import java.math.BigDecimal;
import java.util.List;

public interface TicketService extends Service<Ticket, Integer> {

    //покупка билетов: создаёт заказ и билеты в одной транзакции.
    Order buyTickets(List<TicketDto> tickets);

    List<Ticket> getTicketsByOrderId(Integer orderId);

    int getReservationMinutes();

    BigDecimal calculatePrice(Session session, Place place);

    List<Ticket> findByUserId(Integer userId);

    List<Ticket> findBySessionIdAndStatus(Integer sessionId, TicketStatus status);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findBySessionId(Integer sessionId);

    long countSoldTicketsBySession(Integer sessionId);

    record TicketDto(Integer sessionId, Integer placeId) {
    }
}