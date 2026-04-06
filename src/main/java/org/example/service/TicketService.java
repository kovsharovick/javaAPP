package org.example.service;

import org.example.model.Order;
import org.example.model.Ticket;
import org.example.model.Place;

import java.util.List;

public interface TicketService extends Service<Ticket, Integer> {

    //покупка билетов: создаёт заказ и билеты в одной транзакции.
    Order buyTickets(List<TicketDto> tickets);

    List<Ticket> getTicketsByOrderId(Integer orderId);

    int getReservationMinutes();

    record TicketDto(Integer sessionId, Integer placeId) {
    }
}