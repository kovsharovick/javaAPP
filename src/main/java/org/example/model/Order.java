package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class Order {
    private LocalDateTime reservedUntil; //время до которого зарезервированы места.
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private OrderStatus orderStatus;

    public Order() {
    }

    public Order(Integer id, Integer userId, BigDecimal amount, LocalDateTime dateTime, OrderStatus orderStatus, LocalDateTime reservedUntil) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.orderStatus = orderStatus;
        this.reservedUntil = reservedUntil;
    }

}
