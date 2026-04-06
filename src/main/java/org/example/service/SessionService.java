package org.example.service;

import org.example.model.Session;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionService extends Service<Session, Integer> {

    void createSession(Integer hallId, Integer filmId, LocalDateTime startTime);

    void updateSession(Session session);

    boolean delete(Integer id, boolean deleteWithHistory);

    List<Session> findByFilmId(Integer filmId);

    List<Session> findByHallId(Integer hallId);

    //сеансы в промежуток определенного времени.
    List<Session> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Session> findByFilmIdAndDateRange(Integer filmId, LocalDateTime from, LocalDateTime to);

    boolean existsOverlap(Integer hallId, LocalDateTime startTime, LocalDateTime endTime, Integer excludeSessionId);

    //сеансы после определенного времени.
    List<Session> findUpcoming(LocalDateTime from);

    void markFinishedSessions();
}