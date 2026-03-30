package org.example.repository;

import org.example.model.Orders;

import java.util.List;

public interface OrderRepository extends Repository<Orders> {
    List<Orders> findByUserId(Integer userId);
}