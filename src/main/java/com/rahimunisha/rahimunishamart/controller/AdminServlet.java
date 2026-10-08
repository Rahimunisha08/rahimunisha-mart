package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.dto.UserResponseDTO;
import com.rahimunisha.rahimunishamart.model.Order;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.service.OrderService;
import com.rahimunisha.rahimunishamart.service.ProductService;
import com.rahimunisha.rahimunishamart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin", "/admin/*"})
public class AdminServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserResponseDTO> users = userService.getAllUsers();
        List<Product> products = productService.getAllProductsForAdmin();
        List<Order> orders = orderService.getAllOrdersForAdmin();

        req.setAttribute("users", users);
        req.setAttribute("products", products);
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if ("/products/status".equals(pathInfo)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                String newStatus = req.getParameter("status");
                productService.updateProductStatusByAdmin(productId, newStatus);
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?msg=Listing+moderated+successfully");
                return;

            } else if ("/users/delete".equals(pathInfo)) {
                Long userId = Long.parseLong(req.getParameter("userId"));
                userService.deleteUser(userId);
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?msg=User+removed+successfully");
                return;
            }
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard?error=" + e.getMessage());
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}
