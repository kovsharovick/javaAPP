package org.example.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Film;
import org.example.model.Session;
import org.example.service.FilmService;
import org.example.service.SessionService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/sessions")
public class SessionListServlet extends HttpServlet {
    private SessionService sessionService;
    private FilmService filmService;

    @Override
    public void init() {
        sessionService = (SessionService) getServletContext().getAttribute("sessionService");
        filmService = (FilmService) getServletContext().getAttribute("filmService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filmIdParam = req.getParameter("filmId");
        String dateParam = req.getParameter("date");
        List<Session> sessions;

        LocalDateTime from = null, to = null;
        if (dateParam != null && !dateParam.isEmpty()) {
            LocalDate date = LocalDate.parse(dateParam);
            from = date.atStartOfDay();
            to = date.plusDays(1).atStartOfDay();
        }

        if (filmIdParam != null && !filmIdParam.isEmpty() && dateParam != null && !dateParam.isEmpty()) {
            int filmId = Integer.parseInt(filmIdParam);
            sessions = sessionService.findByFilmIdAndDateRange(filmId, from, to);
        } else if (filmIdParam != null && !filmIdParam.isEmpty()) {
            sessions = sessionService.findByFilmId(Integer.parseInt(filmIdParam));
        } else if (dateParam != null && !dateParam.isEmpty()) {
            sessions = sessionService.findByStartTimeBetween(from, to);
        } else {
            sessions = sessionService.findUpcoming(LocalDateTime.now());
        }

        Map<Integer, Film> filmMap = new HashMap<>();
        for (Session s : sessions) {
            filmMap.put(s.getFilmId(), filmService.getById(s.getFilmId()));
        }

        req.setAttribute("sessions", sessions);
        req.setAttribute("filmMap", filmMap);
        req.getRequestDispatcher("/WEB-INF/jsp/sessionList.jsp").forward(req, resp);
    }
}