package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.model.WishlistItem;
import com.rahimunisha.rahimunishamart.service.WishlistService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist", "/wishlist/*"})
public class WishlistServlet extends HttpServlet {
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {
            req.setAttribute("wishlistItems", java.util.Collections.emptyList());
            req.setAttribute("isGuest", true);
            req.getRequestDispatcher("/WEB-INF/views/buyer/wishlist.jsp").forward(req, resp);
            return;
        }

        List<WishlistItem> items = wishlistService.getWishlist(user.getId());
        req.setAttribute("wishlistItems", items);
        req.getRequestDispatcher("/WEB-INF/views/buyer/wishlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {
            session = req.getSession(true);
            session.setAttribute("authMessage", "Please sign in to manage your wishlist.");
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getContextPath() + "/wishlist");
            return;
        }

        String pathInfo = req.getPathInfo();
        String productIdStr = req.getParameter("productId");
        Long productId = Long.parseLong(productIdStr);

        if ("/add".equals(pathInfo)) {
            wishlistService.addToWishlist(user.getId(), productId);
            String redirect = req.getParameter("redirect");
            if (redirect != null) {
                resp.sendRedirect(redirect + "?wishlist=added");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/wishlist");
        } else if ("/remove".equals(pathInfo)) {
            wishlistService.removeFromWishlist(user.getId(), productId);
            resp.sendRedirect(req.getContextPath() + "/wishlist");
        }
    }
}
