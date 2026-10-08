package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.CartItem;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/*"})
public class CartServlet extends HttpServlet {
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        if ("/count".equals(pathInfo)) {
            int count = cartService.getCartCount(user.getId());
            resp.setContentType("application/json");
            resp.getWriter().write("{\"count\":" + count + "}");
            return;
        }

        List<CartItem> cartItems = cartService.getCart(user.getId());
        BigDecimal total = cartService.getCartTotal(user.getId());

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("cartTotal", total);
        req.getRequestDispatcher("/WEB-INF/views/buyer/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        try {
            if ("/add".equals(pathInfo)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                int quantity = 1;
                String qStr = req.getParameter("quantity");
                if (qStr != null) {
                    quantity = Integer.parseInt(qStr);
                }
                cartService.addToCart(user.getId(), productId, quantity);
                resp.sendRedirect(req.getContextPath() + "/cart?added=true");
                return;

            } else if ("/update".equals(pathInfo)) {
                Long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                cartService.updateQuantity(cartItemId, user.getId(), quantity);
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;

            } else if ("/remove".equals(pathInfo)) {
                Long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                cartService.removeFromCart(cartItemId, user.getId());
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;

            } else if ("/clear".equals(pathInfo)) {
                cartService.clearCart(user.getId());
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
        } catch (ValidationException e) {
            session.setAttribute("cartError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        } catch (Exception e) {
            session.setAttribute("cartError", "An unexpected error occurred: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}
