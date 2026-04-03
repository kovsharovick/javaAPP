package org.example.service.impl;

import org.example.config.DatabaseConnection;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Ticket;
import org.example.repository.OrderRepository;
import org.example.repository.TicketRepository;
import org.example.service.OrderService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    //private static final int RESERVATION_MINUTES = 15;

    public OrderServiceImpl(OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    /*
    @Override
    public Order createOrder(Integer userId) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.WAIT_PAYMENT);
        order.setAmount(BigDecimal.ZERO);
        order.setUserId(userId);
        order.setDateTime(LocalDateTime.now());
        order.setReservedUntil(LocalDateTime.now().plusMinutes(RESERVATION_MINUTES));
        return orderRepository.save(order);
    }
    */

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
        if (order == null) throw new IllegalArgumentException("Заказ не найден");
        if (!isAdmin && !order.getUserId().equals(userId))
            throw new SecurityException("Нет прав на отмену чужого заказа");
        if (order.getOrderStatus() == OrderStatus.COMPLETED) return;

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            orderRepository.cancelOrderWithConnection(conn, orderId);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Ошибка при отмене заказа", e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public OrderWithTickets getOrderWithTickets(Integer orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) throw new IllegalArgumentException("Заказ не найден");
        List<Ticket> tickets = ticketRepository.findByOrderId(orderId);
        return new OrderWithTickets(order, tickets);
    }

    @Override
    public void confirmPayment(Integer orderId, Integer userId, boolean isAdmin) {
        Order order = orderRepository.findById(orderId);
        if (order == null) throw new IllegalArgumentException("Заказ не найден");
        if (!isAdmin && !order.getUserId().equals(userId))
            throw new SecurityException("Нет прав на оплату чужого заказа");
        if (order.getOrderStatus() != OrderStatus.WAIT_PAYMENT)
            throw new IllegalStateException("Заказ не ожидает оплаты");
        if (order.getReservedUntil() == null || order.getReservedUntil().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Время резерва истекло");

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            orderRepository.confirmPaymentWithConnection(conn, orderId);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Ошибка при подтверждении оплаты", e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void cancelExpiredReservations() {
        List<Order> expiredOrders = orderRepository.findExpiredReservations();
        if (expiredOrders.isEmpty()) return;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            for (Order order : expiredOrders) {
                orderRepository.cancelOrderWithConnection(conn, order.getId());
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Ошибка при отмене просроченных резервов", e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
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
