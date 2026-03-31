package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Session {
    private Integer id;
    private Integer hallId;
    private Integer filmId;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    public Session() {}

    public Session(Integer id, Integer hallId, Integer filmId, LocalDateTime startTime, LocalDateTime finishTime) {
        this.id = id;
        this.hallId = hallId;
        this.filmId = filmId;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

}
