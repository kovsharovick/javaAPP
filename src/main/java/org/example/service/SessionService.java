package org.example.service;

import org.example.model.Session;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionService extends Service<Session, Integer> {

    // Создание сеанса (автоматически вычисляется finishTime)
    Session createSession(Integer hallId, Integer filmId, LocalDateTime startTime);

    void updateSession(Session session);

    boolean delete(Integer id);

    List<Session> findByFilmId(Integer filmId);

    List<Session> findByHallId(Integer hallId);

    List<Session> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);

    // Проверка пересечения интервалов
    boolean existsOverlap(Integer hallId, LocalDateTime startTime, LocalDateTime endTime, Integer excludeSessionId);
}