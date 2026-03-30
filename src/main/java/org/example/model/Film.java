package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;

@Setter
@Getter
public class Film {
    private Integer id;
    private String name;
    private Duration duration;
    private BigDecimal price;
    private String description;
    private String posterUrl;

    public Film() {}

    public Film(Integer id, String name, Duration duration, BigDecimal price, String description, String posterUrl) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;
        this.description = description;
        this.posterUrl = posterUrl;
    }

}
