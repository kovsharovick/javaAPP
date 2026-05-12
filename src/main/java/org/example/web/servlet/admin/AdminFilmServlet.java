package org.example.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Film;
import org.example.service.FilmService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@WebServlet("/admin/films")
public class AdminFilmServlet extends HttpServlet {

    private FilmService filmService;

    @Override
    public void init() {
        filmService = (FilmService) getServletContext().getAttribute("filmService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            req.getRequestDispatcher("/WEB-INF/jsp/admin/filmForm.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                Film film = filmService.getById(id);
                req.setAttribute("film", film);
                req.getRequestDispatcher("/WEB-INF/jsp/admin/filmForm.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            List<Film> films = filmService.getAll();
            req.setAttribute("films", films);
            req.getRequestDispatcher("/WEB-INF/jsp/admin/filmList.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                String name = req.getParameter("name");
                int duration = Integer.parseInt(req.getParameter("duration"));
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                String description = req.getParameter("description");
                String posterUrl = req.getParameter("posterUrl");

                Film film = new Film();
                film.setName(name);
                film.setDuration(Duration.ofMinutes(duration));
                film.setPrice(price);
                film.setDescription(description);
                film.setPosterUrl(posterUrl);
                filmService.save(film);
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Film film = filmService.getById(id);
                if (film != null) {
                    if (req.getParameter("name") != null && !req.getParameter("name").isEmpty())
                        film.setName(req.getParameter("name"));
                    if (req.getParameter("duration") != null && !req.getParameter("duration").isEmpty())
                        film.setDuration(Duration.ofMinutes(Integer.parseInt(req.getParameter("duration"))));
                    if (req.getParameter("price") != null && !req.getParameter("price").isEmpty())
                        film.setPrice(new BigDecimal(req.getParameter("price")));
                    if (req.getParameter("description") != null)
                        film.setDescription(req.getParameter("description"));
                    if (req.getParameter("posterUrl") != null)
                        film.setPosterUrl(req.getParameter("posterUrl"));
                    filmService.update(film);
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                filmService.delete(id);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/films");
    }
}