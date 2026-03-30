package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Hall {
    private Integer id;
    private int rows;
    private int seatsPerRow;
    private BigDecimal price;

    public Hall() {}

    public Hall(Integer id, int rows, int seatsPerRow, BigDecimal price) {
        this.id = id;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.price = price;
    }

}
