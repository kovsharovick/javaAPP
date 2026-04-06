package org.example.service;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends Service<Order, Integer> {

    //Order createOrder(Integer userId);

    List<Order> findByUserId(Integer userId);

    void updateOrderStatus(Integer orderId, OrderStatus status);

    void cancelOrder(Integer orderId, Integer userId, boolean isAdmin);

    OrderWithTickets getOrderWithTickets(Integer orderId);

    void confirmPayment(Integer orderId, Integer userId, boolean isAdmin); //симуляция оплаты.

    void cancelExpiredReservations();

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUserIdAndStatus(Integer userId, OrderStatus status);

    BigDecimal sumRevenueByPeriod(LocalDateTime from, LocalDateTime to);

    record OrderWithTickets(Order order, List<Ticket> tickets) {
    }
}