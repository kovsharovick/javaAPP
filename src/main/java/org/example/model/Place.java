package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Place {
    private Integer id;
    private Integer hallId;
    private int rows;
    private int seat;
    private TypePlace typePlace;

    public Place() {}

    public Place(Integer id, Integer hallId, int rows, int seat, TypePlace typePlace) {
        this.id = id;
        this.hallId = hallId;
        this.rows = rows;
        this.seat = seat;
        this.typePlace = typePlace;
    }

}
