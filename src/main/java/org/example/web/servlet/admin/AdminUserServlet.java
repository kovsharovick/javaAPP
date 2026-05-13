package org.example.web.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.getAll();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/userList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String userIdStr = req.getParameter("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан пользователь");
            return;
        }
        int userId = Integer.parseInt(userIdStr);
        User targetUser = userService.getById(userId);
        if (targetUser == null) {
            req.setAttribute("error", "Пользователь не найден");
            doGet(req, resp);
            return;
        }

        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            if ("grant".equals(action)) {
                if (targetUser.getAdmin()) {
                    req.setAttribute("error", "Пользователь уже является администратором");
                } else {
                    userService.updateStatus(targetUser, true);
                    req.setAttribute("message", "Права администратора назначены");
                }
            } else if ("revoke".equals(action)) {
                if (!targetUser.getAdmin()) {
                    req.setAttribute("error", "Пользователь не является администратором");
                } else if (targetUser.getId().equals(currentUser.getId())) {
                    req.setAttribute("error", "Нельзя снять права администратора с самого себя");
                } else {
                    userService.updateStatus(targetUser, false);
                    req.setAttribute("message", "Права администратора сняты");
                }
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        doGet(req, resp);
    }
}