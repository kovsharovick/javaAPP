package org.example.repository;

import org.example.model.Order;

import java.util.List;

public interface OrderRepository extends Repository<Order> {
    List<Order> findByUserId(Integer userId);
}