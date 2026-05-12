package org.example.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Ticket;
import org.example.model.TicketStatus;
import org.example.service.TicketService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/tickets")
public class AdminTicketListServlet extends HttpServlet {

    private TicketService ticketService;

    @Override
    public void init() {
        ticketService = (TicketService) getServletContext().getAttribute("ticketService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionIdParam = req.getParameter("sessionId");
        String userIdParam = req.getParameter("userId");
        String statusParam = req.getParameter("status");
        List<Ticket> tickets = null;

        try {
            if (sessionIdParam != null && statusParam != null) {
                tickets = ticketService.findBySessionIdAndStatus(Integer.parseInt(sessionIdParam), TicketStatus.valueOf(statusParam));
            } else if (sessionIdParam != null) {
                tickets = ticketService.findBySessionId(Integer.parseInt(sessionIdParam));
            } else if (statusParam != null) {
                tickets = ticketService.findByStatus(TicketStatus.valueOf(statusParam));
            } else if (userIdParam != null) {
                tickets = ticketService.findByUserId(Integer.parseInt(userIdParam));
            } else {
                tickets = ticketService.getAll();
            }
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный параметр");
            return;
        }
        req.setAttribute("tickets", tickets);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/ticketList.jsp").forward(req, resp);
    }
}