package org.example.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Film;
import org.example.service.FilmService;

import java.io.IOException;
import java.util.List;

@WebServlet("/films")
public class FilmListServlet extends HttpServlet {

    private FilmService filmService;

    @Override
    public void init() {
        filmService = (FilmService) getServletContext().getAttribute("filmService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        List<Film> films;
        if (search != null && !search.isEmpty()) {
            films = filmService.findByNameContaining(search);
        } else {
            films = filmService.getAll();
        }
        req.setAttribute("films", films);
        req.getRequestDispatcher("/WEB-INF/jsp/filmList.jsp").forward(req, resp);
    }
}