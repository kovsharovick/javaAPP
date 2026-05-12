package org.example.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Place;
import org.example.service.PlaceService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/sessionPlaces")
public class SessionPlacesServlet extends HttpServlet {

    private PlaceService placeService;

    @Override
    public void init() {
        placeService = (PlaceService) getServletContext().getAttribute("placeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionIdParam = req.getParameter("sessionId");
        if (sessionIdParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int sessionId = Integer.parseInt(sessionIdParam);
        List<Place> freePlaces = placeService.getFreePlacesForSession(sessionId);

        Map<Integer, List<Place>> byRow = freePlaces.stream()
                .collect(Collectors.groupingBy(Place::getRows));
        req.setAttribute("sessionId", sessionId);
        req.setAttribute("byRow", byRow);
        req.getRequestDispatcher("/WEB-INF/jsp/sessionPlaces.jsp").forward(req, resp);
    }
}