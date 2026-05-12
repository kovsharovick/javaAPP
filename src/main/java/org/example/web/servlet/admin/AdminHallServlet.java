package org.example.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Hall;
import org.example.service.HallService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/halls")
public class AdminHallServlet extends HttpServlet {

    private HallService hallService;

    @Override
    public void init() {
        hallService = (HallService) getServletContext().getAttribute("hallService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            req.getRequestDispatcher("/WEB-INF/jsp/admin/hallForm.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                Hall hall = hallService.getById(id);
                req.setAttribute("hall", hall);
                req.getRequestDispatcher("/WEB-INF/jsp/admin/hallForm.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            List<Hall> halls = hallService.getAll();
            req.setAttribute("halls", halls);
            req.getRequestDispatcher("/WEB-INF/jsp/admin/hallList.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                int rows = Integer.parseInt(req.getParameter("rows"));
                int seatsPerRow = Integer.parseInt(req.getParameter("seatsPerRow"));
                BigDecimal price = new BigDecimal(req.getParameter("price"));

                Hall hall = new Hall();
                hall.setRows(rows);
                hall.setSeatsPerRow(seatsPerRow);
                hall.setPrice(price);
                hallService.save(hall);
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Hall hall = hallService.getById(id);
                if (hall != null) {
                    if (req.getParameter("rows") != null && !req.getParameter("rows").isEmpty())
                        hall.setRows(Integer.parseInt(req.getParameter("rows")));
                    if (req.getParameter("seatsPerRow") != null && !req.getParameter("seatsPerRow").isEmpty())
                        hall.setSeatsPerRow(Integer.parseInt(req.getParameter("seatsPerRow")));
                    if (req.getParameter("price") != null && !req.getParameter("price").isEmpty())
                        hall.setPrice(new BigDecimal(req.getParameter("price")));
                    hallService.update(hall);
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                hallService.delete(id);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/halls");
    }
}