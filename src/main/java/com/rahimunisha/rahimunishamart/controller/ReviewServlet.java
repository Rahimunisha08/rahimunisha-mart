package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.service.ReviewService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ReviewServlet", urlPatterns = {"/reviews/add"})
public class ReviewServlet extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String productIdStr = req.getParameter("productId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");

        try {
            Long productId = Long.parseLong(productIdStr);
            int rating = Integer.parseInt(ratingStr);
            reviewService.submitReview(user.getId(), productId, rating, comment);
            resp.sendRedirect(req.getContextPath() + "/products/" + productId + "?reviewSuccess=true");
        } catch (ValidationException e) {
            resp.sendRedirect(req.getContextPath() + "/products/" + productIdStr + "?reviewError=" + e.getMessage());
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/products/" + productIdStr + "?reviewError=Invalid+submission");
        }
    }
}
