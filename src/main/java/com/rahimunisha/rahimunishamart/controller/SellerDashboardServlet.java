package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.Order;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.service.OrderService;
import com.rahimunisha.rahimunishamart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "SellerDashboardServlet", urlPatterns = {"/seller", "/seller/*"})
public class SellerDashboardServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/dashboard")) {
            showDashboard(req, resp, user);
            return;
        }

        if ("/products/new".equals(pathInfo)) {
            req.setAttribute("categories", productService.getCategories());
            req.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(req, resp);
            return;
        }

        if ("/products/edit".equals(pathInfo)) {
            try {
                Long productId = Long.parseLong(req.getParameter("id"));
                Product product = productService.getProductById(productId);
                req.setAttribute("product", product);
                req.setAttribute("categories", productService.getCategories());
                req.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?error=" + e.getMessage());
            }
            return;
        }

        if ("/orders".equals(pathInfo)) {
            List<Order> orders = orderService.getSellerOrders(user.getId());
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/WEB-INF/views/seller/orders.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String pathInfo = req.getPathInfo();

        if ("/products/new".equals(pathInfo)) {
            handleCreateProduct(req, resp, user);
        } else if ("/products/edit".equals(pathInfo)) {
            handleEditProduct(req, resp, user);
        } else if ("/products/delete".equals(pathInfo)) {
            handleDeleteProduct(req, resp, user);
        } else if ("/orders/status".equals(pathInfo)) {
            handleUpdateOrderStatus(req, resp, user);
        } else {
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        List<Product> products = productService.getSellerProducts(user.getId());
        List<Order> incomingOrders = orderService.getSellerOrders(user.getId());

        // Calculate sales metrics (O3)
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalItemsSold = 0;
        int lowStockCount = 0;

        for (Product p : products) {
            if (p.getStockQty() != null && p.getStockQty() <= 5) {
                lowStockCount++;
            }
        }

        for (Order o : incomingOrders) {
            if (!"CANCELLED".equalsIgnoreCase(o.getStatus())) {
                totalRevenue = totalRevenue.add(o.getTotalAmount());
                totalItemsSold += o.getItems().size();
            }
        }

        req.setAttribute("products", products);
        req.setAttribute("incomingOrders", incomingOrders);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalItemsSold", totalItemsSold);
        req.setAttribute("lowStockCount", lowStockCount);

        req.getRequestDispatcher("/WEB-INF/views/seller/dashboard.jsp").forward(req, resp);
    }

    private void handleCreateProduct(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String category = req.getParameter("category");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stockQty");
        String imageUrl = req.getParameter("imageUrl");

        try {
            BigDecimal price = new BigDecimal(priceStr);
            int stock = Integer.parseInt(stockStr);
            productService.createProduct(user.getId(), name, description, category, price, stock, imageUrl);
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=Product+listed+successfully");
        } catch (ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("fieldErrors", e.getFieldErrors());
            req.setAttribute("categories", productService.getCategories());
            req.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Invalid numeric input: " + e.getMessage());
            req.setAttribute("categories", productService.getCategories());
            req.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(req, resp);
        }
    }

    private void handleEditProduct(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String category = req.getParameter("category");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stockQty");
        String imageUrl = req.getParameter("imageUrl");
        String status = req.getParameter("status");

        try {
            Long productId = Long.parseLong(idStr);
            BigDecimal price = new BigDecimal(priceStr);
            int stock = Integer.parseInt(stockStr);
            productService.updateProduct(user.getId(), productId, name, description, category, price, stock, imageUrl, status);
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=Product+updated+successfully");
        } catch (ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("fieldErrors", e.getFieldErrors());
            req.setAttribute("categories", productService.getCategories());
            req.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Failed to update: " + e.getMessage());
            req.setAttribute("categories", productService.getCategories());
            req.getRequestDispatcher("/WEB-INF/views/seller/product-form.jsp").forward(req, resp);
        }
    }

    private void handleDeleteProduct(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        try {
            Long productId = Long.parseLong(req.getParameter("id"));
            productService.deleteProduct(user.getId(), productId);
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard?deleted=true");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard?error=" + e.getMessage());
        }
    }

    private void handleUpdateOrderStatus(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            String newStatus = req.getParameter("status");
            orderService.updateOrderStatus(orderId, newStatus, user.getId(), user.getRole());
            resp.sendRedirect(req.getContextPath() + "/seller/orders?updated=true");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/seller/orders?error=" + e.getMessage());
        }
    }
}
