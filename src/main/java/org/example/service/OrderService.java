package org.example.service;

import org.example.model.Order;
import org.example.model.OrderStatus;

import java.util.List;

public interface OrderService extends Service<Order, Integer> {
    // Создаёт пустой заказ (сумма 0, статус CREATED)
    Order createOrder(Integer userId);

    List<Order> findByUserId(Integer userId);

    void updateOrderStatus(Integer orderId, OrderStatus status);

    // Отмена заказа (если пользователь имеет право)
    boolean cancelOrder(Integer orderId, Integer userId, boolean isStaff);
}