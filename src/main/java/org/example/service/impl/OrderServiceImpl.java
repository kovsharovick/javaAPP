package org.example.service.impl;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Ticket;
import org.example.model.TicketStatus;
import org.example.repository.OrderRepository;
import org.example.repository.TicketRepository;
import org.example.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    public OrderServiceImpl(OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Order createOrder(Integer userId) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.WAIT_PAYMENT);
        order.setAmount(BigDecimal.ZERO);
        order.setUserId(userId);
        order.setDateTime(LocalDateTime.now());
        return orderRepository.save(order);
    }


    @Override
    public List<Order> findByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void updateOrderStatus(Integer orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Заказ не найден");
        }
        order.setOrderStatus(status);
        orderRepository.update(order);
    }

    @Override
    public void cancelOrder(Integer orderId, Integer userId, boolean isAdmin) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Заказ не найден");
        }
        if (!isAdmin && !order.getUserId().equals(userId)) {
            throw new SecurityException("Нет прав на отмену чужого заказа");
        }
        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            return;
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        List<Ticket> tickets = ticketRepository.findByOrderId(orderId);
        for (Ticket ticket : tickets) {
            ticket.setTicketStatus(TicketStatus.CANCELED);
            ticketRepository.update(ticket);
        }
        orderRepository.update(order);
    }

    @Override
    public OrderWithTickets getOrderWithTickets(Integer orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) throw new IllegalArgumentException("Заказ не найден");
        List<Ticket> tickets = ticketRepository.findByOrderId(orderId);
        return new OrderWithTickets(order, tickets);
    }

    @Override
    public void confirmPayment(Integer orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) throw new IllegalArgumentException("Заказ не найден");
        if (order.getOrderStatus() != OrderStatus.WAIT_PAYMENT) {
            throw new IllegalStateException("Заказ не ожидает оплаты");
        }
        if (order.getReservedUntil().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Время резерва истекло");
        }

        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.update(order);

        List<Ticket> tickets = ticketRepository.findByOrderId(orderId);
        for (Ticket ticket : tickets) {
            if (ticket.getTicketStatus() == TicketStatus.RESERVED) {
                ticket.setTicketStatus(TicketStatus.SOLD);
                ticketRepository.update(ticket);
            }
        }
    }

    @Override
    public void cancelExpiredReservations() {
        List<Order> allOrders = orderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Order order : allOrders) {
            if (order.getOrderStatus() == OrderStatus.WAIT_PAYMENT && order.getReservedUntil().isBefore(now)) {
                cancelOrder(order.getId(), order.getUserId(), true);
            }
        }
    }

    @Override
    public Order getById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}
