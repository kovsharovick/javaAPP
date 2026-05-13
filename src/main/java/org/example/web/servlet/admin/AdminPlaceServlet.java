package org.example.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Hall;
import org.example.model.Place;
import org.example.model.TypePlace;
import org.example.service.HallService;
import org.example.service.PlaceService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin/places")
public class AdminPlaceServlet extends HttpServlet {

    private PlaceService placeService;
    private HallService hallService;

    @Override
    public void init() {
        placeService = (PlaceService) getServletContext().getAttribute("placeService");
        hallService = (HallService) getServletContext().getAttribute("hallService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hallIdParam = req.getParameter("hallId");
        if (hallIdParam != null) {
            int hallId = Integer.parseInt(hallIdParam);
            Hall hall = hallService.getById(hallId);
            List<Place> places = placeService.findByHallId(hallId);
            Map<Integer, List<Place>> rowsMap = places.stream()
                    .collect(Collectors.groupingBy(Place::getRows));
            req.setAttribute("hall", hall);
            req.setAttribute("places", places);
            req.setAttribute("rowsMap", rowsMap);
            req.setAttribute("hallId", hallId);
            req.getRequestDispatcher("/WEB-INF/jsp/admin/placeList.jsp").forward(req, resp);
        } else {
            List<Hall> halls = hallService.getAll();
            req.setAttribute("halls", halls);
            req.getRequestDispatcher("/WEB-INF/jsp/admin/selectHall.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("generate".equals(action)) {
                int hallId = Integer.parseInt(req.getParameter("hallId"));
                placeService.generatePlacesForHall(hallId);
            } else if ("updateType".equals(action)) {
                int placeId = Integer.parseInt(req.getParameter("placeId"));
                String typeStr = req.getParameter("type");
                Place place = placeService.getById(placeId);
                place.setTypePlace(TypePlace.valueOf(typeStr));
                placeService.update(place);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/places?hallId=" + req.getParameter("hallId"));
    }
}