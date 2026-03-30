package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Ticket {
    private Integer id;
    private Integer ordersId;
    private Integer placeId;
    private Integer sessionId;
    private BigDecimal price;
    private TicketStatus ticketStatus;

    public Ticket() {}

    public Ticket(Integer id, Integer ordersId, Integer placeId, Integer sessionId, BigDecimal price, TicketStatus ticketStatus) {
        this.id = id;
        this.ordersId = ordersId;
        this.placeId = placeId;
        this.sessionId = sessionId;
        this.price = price;
        this.ticketStatus = ticketStatus;
    }

}
