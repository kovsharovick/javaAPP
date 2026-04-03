package org.example.repository;

import org.example.model.Session;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends Repository<Session> {
    List<Session> findByFilmId(Integer filmId);

    List<Session> findByHallId(Integer hallId);

    List<Session> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);

    boolean existsOverlap(Integer hallId, LocalDateTime startTime, LocalDateTime endTime, Integer excludeSessionId);

    List<Session> findByStartTimeAfter(LocalDateTime time);
}