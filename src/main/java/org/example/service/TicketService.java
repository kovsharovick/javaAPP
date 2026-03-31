package org.example.service;

import org.example.model.Order;
import org.example.model.Ticket;
import org.example.model.Place;
import java.util.List;

public interface TicketService extends Service<Ticket, Integer>{
    // Покупка билетов: создаёт заказ и билеты в одной транзакции
    Order buyTickets(int userId, List<TicketDto> tickets);
    
    List<Ticket> getTicketsByOrderId(Integer orderId);
    
    // Отмена заказа (освобождает места)
    boolean cancelOrder(Integer orderId, Integer userId, boolean isStaff);
    
    // Свободные места на сеанс
    List<Place> getFreePlaces(int sessionId);
    
    record TicketDto(int sessionId, int placeId) {}
}