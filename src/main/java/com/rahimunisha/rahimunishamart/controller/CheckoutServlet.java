package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.CartItem;
import com.rahimunisha.rahimunishamart.model.Order;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.service.CartService;
import com.rahimunisha.rahimunishamart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        List<CartItem> cartItems = cartService.getCart(user.getId());
        if (cartItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart?empty=true");
            return;
        }

        BigDecimal total = cartService.getCartTotal(user.getId());

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("cartTotal", total);
        req.setAttribute("userAddress", user.getAddress());
        req.getRequestDispatcher("/WEB-INF/views/buyer/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String shippingAddress = req.getParameter("shippingAddress");
        String paymentMethod = req.getParameter("paymentMethod");

        try {
            Order order = orderService.checkoutCart(user.getId(), shippingAddress, paymentMethod);
            resp.sendRedirect(req.getContextPath() + "/orders/" + order.getId() + "?success=true");
        } catch (ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("fieldErrors", e.getFieldErrors());
            List<CartItem> cartItems = cartService.getCart(user.getId());
            BigDecimal total = cartService.getCartTotal(user.getId());
            req.setAttribute("cartItems", cartItems);
            req.setAttribute("cartTotal", total);
            req.setAttribute("userAddress", shippingAddress);
            req.getRequestDispatcher("/WEB-INF/views/buyer/checkout.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Checkout failed: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/buyer/checkout.jsp").forward(req, resp);
        }
    }
}
