package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class Orders {
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private OrderStatus orderStatus;

    public Orders() {}

    public Orders(Integer id, Integer userId, BigDecimal amount, LocalDateTime dateTime, OrderStatus orderStatus) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.orderStatus = orderStatus;
    }

}
