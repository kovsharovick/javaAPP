package org.example.service;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Ticket;

import java.util.List;

public interface OrderService extends Service<Order, Integer> {

    //Order createOrder(Integer userId);

    List<Order> findByUserId(Integer userId);

    void updateOrderStatus(Integer orderId, OrderStatus status);

    void cancelOrder(Integer orderId, Integer userId, boolean isAdmin);

    OrderWithTickets getOrderWithTickets(Integer orderId);

    record OrderWithTickets(Order order, List<Ticket> tickets) {};

    void confirmPayment(Integer orderId, Integer userId, boolean isAdmin); //симуляция оплаты.

    void cancelExpiredReservations();
}