package org.example.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Film;
import org.example.model.Hall;
import org.example.model.Session;
import org.example.service.FilmService;
import org.example.service.HallService;
import org.example.service.SessionService;

import java.io.IOException;

@WebServlet("/session")
public class SessionInfoServlet extends HttpServlet {

    private SessionService sessionService;
    private FilmService filmService;
    private HallService hallService;

    @Override
    public void init() {
        sessionService = (SessionService) getServletContext().getAttribute("sessionService");
        filmService = (FilmService) getServletContext().getAttribute("filmService");
        hallService = (HallService) getServletContext().getAttribute("hallService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(idParam);
        Session session = sessionService.getById(id);
        if (session == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Film film = filmService.getById(session.getFilmId());
        Hall hall = hallService.getById(session.getHallId());
        req.setAttribute("session", session);
        req.setAttribute("film", film);
        req.setAttribute("hall", hall);
        req.getRequestDispatcher("/WEB-INF/jsp/sessionInfo.jsp").forward(req, resp);
    }
}