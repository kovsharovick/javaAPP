package org.example.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Place;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.service.PlaceService;
import org.example.service.TicketService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/myTickets")
public class MyTicketsServlet extends HttpServlet {

    private TicketService ticketService;
    private PlaceService placeService;

    @Override
    public void init() {
        ticketService = (TicketService) getServletContext().getAttribute("ticketService");
        placeService = (PlaceService) getServletContext().getAttribute("placeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        List<Ticket> tickets = ticketService.findByUserId(user.getId());

        Map<Integer, Place> placeMap = new HashMap<>();
        for (Ticket t : tickets) {
            Place p = placeService.getById(t.getPlaceId());
            if (p != null) {
                placeMap.put(t.getPlaceId(), p);
            }
        }
        req.setAttribute("tickets", tickets);
        req.setAttribute("placeMap", placeMap);
        req.getRequestDispatcher("/WEB-INF/jsp/myTickets.jsp").forward(req, resp);
    }
}