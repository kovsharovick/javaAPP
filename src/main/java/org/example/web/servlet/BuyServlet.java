package org.example.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Order;
import org.example.model.User;
import org.example.service.TicketService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/buy")
public class BuyServlet extends HttpServlet {

    private TicketService ticketService;

    @Override
    public void init() {
        ticketService = (TicketService) getServletContext().getAttribute("ticketService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String sessionIdParam = req.getParameter("sessionId");
        String[] placeIds = req.getParameterValues("placeId");
        if (sessionIdParam == null || placeIds == null || placeIds.length == 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не выбраны места");
            return;
        }
        int sessionId = Integer.parseInt(sessionIdParam);
        List<TicketService.TicketDto> tickets = new ArrayList<>();
        for (String pid : placeIds) {
            tickets.add(new TicketService.TicketDto(sessionId, Integer.parseInt(pid)));
        }
        try {
            Order order = ticketService.buyTickets(tickets);
            int reservationMinutes = ticketService.getReservationMinutes();
            req.setAttribute("reservationMinutes", reservationMinutes);
            req.setAttribute("order", order);
            req.getRequestDispatcher("/WEB-INF/jsp/orderCreated.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/sessionPlaces?sessionId=" + sessionId);
        }
    }
}