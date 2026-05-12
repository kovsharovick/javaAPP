package org.example.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Session;
import org.example.service.FilmService;
import org.example.service.HallService;
import org.example.service.SessionService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/admin/sessions")
public class AdminSessionServlet extends HttpServlet {

    private SessionService sessionService;
    private FilmService filmService;
    private HallService hallService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() {
        sessionService = (SessionService) getServletContext().getAttribute("sessionService");
        filmService = (FilmService) getServletContext().getAttribute("filmService");
        hallService = (HallService) getServletContext().getAttribute("hallService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        req.setAttribute("films", filmService.getAll());
        req.setAttribute("halls", hallService.getAll());

        if ("create".equals(action)) {
            req.getRequestDispatcher("/WEB-INF/jsp/admin/sessionForm.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                Session session = sessionService.getById(Integer.parseInt(idParam));
                req.setAttribute("session", session);
                req.getRequestDispatcher("/WEB-INF/jsp/admin/sessionForm.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            List<Session> sessions = sessionService.getAll();
            req.setAttribute("sessions", sessions);
            req.getRequestDispatcher("/WEB-INF/jsp/admin/sessionList.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                int hallId = Integer.parseInt(req.getParameter("hallId"));
                int filmId = Integer.parseInt(req.getParameter("filmId"));
                LocalDateTime startTime = LocalDateTime.parse(req.getParameter("startTime"), FORMATTER);
                sessionService.createSession(hallId, filmId, startTime);
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Session session = sessionService.getById(id);
                if (session != null) {
                    if (req.getParameter("hallId") != null && !req.getParameter("hallId").isEmpty())
                        session.setHallId(Integer.parseInt(req.getParameter("hallId")));
                    if (req.getParameter("filmId") != null && !req.getParameter("filmId").isEmpty())
                        session.setFilmId(Integer.parseInt(req.getParameter("filmId")));
                    if (req.getParameter("startTime") != null && !req.getParameter("startTime").isEmpty())
                        session.setStartTime(LocalDateTime.parse(req.getParameter("startTime"), FORMATTER));
                    sessionService.updateSession(session);
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                boolean force = "true".equals(req.getParameter("force"));
                sessionService.delete(id, force);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/sessions");
    }
}