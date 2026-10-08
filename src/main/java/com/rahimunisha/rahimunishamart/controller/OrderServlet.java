package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.exception.ResourceNotFoundException;
import com.rahimunisha.rahimunishamart.exception.UnauthorizedException;
import com.rahimunisha.rahimunishamart.model.Order;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/orders/*"})
public class OrderServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Buyer order history list
            List<Order> orders = orderService.getBuyerOrders(user.getId());
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/WEB-INF/views/buyer/order-history.jsp").forward(req, resp);
            return;
        }

        try {
            Long orderId = Long.parseLong(pathInfo.substring(1));
            Order order = orderService.getOrderDetails(orderId, user.getId(), user.getRole());
            req.setAttribute("order", order);
            String success = req.getParameter("success");
            if ("true".equalsIgnoreCase(success)) {
                req.setAttribute("successMessage", "Order #" + orderId + " has been placed successfully!");
            }
            req.getRequestDispatcher("/WEB-INF/views/buyer/order-detail.jsp").forward(req, resp);

        } catch (NumberFormatException | ResourceNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
        } catch (UnauthorizedException e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.endsWith("/cancel")) {
            try {
                String idStr = pathInfo.replace("/cancel", "").replace("/", "");
                Long orderId = Long.parseLong(idStr);
                orderService.updateOrderStatus(orderId, "CANCELLED", user.getId(), user.getRole());
                resp.sendRedirect(req.getContextPath() + "/orders?cancelled=true");
                return;
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/orders?error=" + e.getMessage());
                return;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
