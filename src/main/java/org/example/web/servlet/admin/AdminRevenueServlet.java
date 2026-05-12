package org.example.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/admin/revenue")
public class AdminRevenueServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = (OrderService) getServletContext().getAttribute("orderService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fromStr = req.getParameter("from");
        String toStr = req.getParameter("to");
        LocalDateTime from = null, to = null;
        if (fromStr != null && !fromStr.isEmpty()) {
            from = LocalDate.parse(fromStr).atStartOfDay();
        }
        if (toStr != null && !toStr.isEmpty()) {
            to = LocalDate.parse(toStr).plusDays(1).atStartOfDay();
        }
        BigDecimal revenue = orderService.sumRevenueByPeriod(from, to);
        req.setAttribute("revenue", revenue);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/revenue.jsp").forward(req, resp);
    }
}