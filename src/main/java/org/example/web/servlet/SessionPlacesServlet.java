package org.example.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.*;
import org.example.service.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/sessionPlaces")
public class SessionPlacesServlet extends HttpServlet {

    private PlaceService placeService;
    private SessionService sessionService;
    private TicketService ticketService;
    private FilmService filmService;
    private HallService hallService;

    @Override
    public void init() {
        placeService = (PlaceService) getServletContext().getAttribute("placeService");
        sessionService = (SessionService) getServletContext().getAttribute("sessionService");
        ticketService = (TicketService) getServletContext().getAttribute("ticketService");
        filmService = (FilmService) getServletContext().getAttribute("filmService");
        hallService = (HallService) getServletContext().getAttribute("hallService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionIdParam = req.getParameter("sessionId");
        if (sessionIdParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int sessionId = Integer.parseInt(sessionIdParam);
        Session session = sessionService.getById(sessionId);
        if (session == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Film film = filmService.getById(session.getFilmId());
        Hall hall = hallService.getById(session.getHallId());
        if (film == null || hall == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не удалось загрузить фильм или зал");
            return;
        }

        BigDecimal basePrice = film.getPrice().add(hall.getPrice().multiply(BigDecimal.valueOf(0.5)));

        List<Place> allPlaces = placeService.findByHallId(session.getHallId());
        List<Ticket> tickets = ticketService.findBySessionId(sessionId);
        Set<Integer> takenPlaceIds = tickets.stream()
                .filter(t -> t.getTicketStatus() == TicketStatus.RESERVED || t.getTicketStatus() == TicketStatus.SOLD)
                .map(Ticket::getPlaceId)
                .collect(Collectors.toSet());

        Map<Integer, List<Place>> byRow = allPlaces.stream()
                .collect(Collectors.groupingBy(Place::getRows));

        req.setAttribute("byRow", byRow);
        req.setAttribute("takenPlaceIds", takenPlaceIds);
        req.setAttribute("sessionId", sessionId);
        req.setAttribute("reservationMinutes", ticketService.getReservationMinutes());
        req.setAttribute("basePrice", basePrice);

        req.getRequestDispatcher("/WEB-INF/jsp/sessionPlaces.jsp").forward(req, resp);
    }
}