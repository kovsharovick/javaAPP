package org.example.service.impl;

import org.example.config.DatabaseConnection;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.repository.OrderRepository;
import org.example.repository.TicketRepository;
import org.example.service.AuthContext;
import org.example.service.OrderService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final AuthContext authContext;

    public OrderServiceImpl(OrderRepository orderRepository, TicketRepository ticketRepository, AuthContext authContext) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.authContext = authContext;
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
        User current = authContext.getCurrentUser();
        if (current == null || (!authContext.isAdmin() && !current.getId().equals(userId))) {
            throw new SecurityException("Нет прав на просмотр этих заказов");
        }
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        if (status == null) {
            throw new IllegalArgumentException("Неверно указан статус");
        }
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> findByUserIdAndStatus(Integer userId, OrderStatus status) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        if (status == null) {
            throw new IllegalArgumentException("Неверно указан статус");
        }
        return orderRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public void updateOrderStatus(Integer orderId, OrderStatus status) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
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
        Order order = getById(orderId);
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
    public BigDecimal sumRevenueByPeriod(LocalDateTime from, LocalDateTime to) {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        return orderRepository.sumRevenueByPeriod(Objects.requireNonNullElseGet(from, () -> LocalDateTime.of(1900, 1, 1, 1, 1)), to);
    }

    @Override
    public Order getById(Integer id) {
        Order order = orderRepository.findById(id);
        if (order == null) throw new IllegalArgumentException("Заказ не найден");
        User current = authContext.getCurrentUser();
        if (current == null || (!authContext.isAdmin() && !current.getId().equals(order.getUserId()))) {
            throw new SecurityException("Нет прав на просмотр этого заказа");
        }
        return order;
    }

    @Override
    public List<Order> getAll() {
        if (!authContext.isAdmin()) throw new SecurityException("Доступно только администраторам!");
        return orderRepository.findAll();
    }
}
