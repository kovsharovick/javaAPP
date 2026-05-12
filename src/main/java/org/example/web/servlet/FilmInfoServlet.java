package org.example.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Film;
import org.example.service.FilmService;

import java.io.IOException;

@WebServlet("/film")
public class FilmInfoServlet extends HttpServlet {

    private FilmService filmService;

    @Override
    public void init() {
        filmService = (FilmService) getServletContext().getAttribute("filmService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(idParam);
        Film film = filmService.getById(id);
        if (film == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("film", film);
        req.getRequestDispatcher("/WEB-INF/jsp/filmInfo.jsp").forward(req, resp);
    }
}