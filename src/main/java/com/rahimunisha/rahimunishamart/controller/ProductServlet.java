package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.dto.ApiResponse;
import com.rahimunisha.rahimunishamart.dto.ProductDTO;
import com.rahimunisha.rahimunishamart.exception.ResourceNotFoundException;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.model.Review;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.service.ProductService;
import com.rahimunisha.rahimunishamart.service.ReviewService;
import com.rahimunisha.rahimunishamart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products/*", "/api/v1/products/*"})
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        boolean isApi = uri.contains("/api/v1/");

        String pathInfo = req.getPathInfo();
        if (isApi) {
            handleApiGet(req, resp, pathInfo);
        } else {
            handleViewGet(req, resp, pathInfo);
        }
    }

    private void handleViewGet(HttpServletRequest req, HttpServletResponse resp, String pathInfo)
            throws ServletException, IOException {
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            Long productId = Long.parseLong(pathInfo.substring(1));
            Product product = productService.getProductById(productId);
            List<Review> reviews = reviewService.getProductReviews(productId);

            HttpSession session = req.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
            boolean canReview = false;
            if (currentUser != null) {
                canReview = reviewService.canUserReview(currentUser.getId(), productId);
            }

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("canReview", canReview);
            req.getRequestDispatcher("/WEB-INF/views/buyer/product-detail.jsp").forward(req, resp);

        } catch (NumberFormatException | ResourceNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product listing not found");
        }
    }

    private void handleApiGet(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                String keyword = req.getParameter("q");
                String category = req.getParameter("category");
                List<ProductDTO> list = productService.getProducts(keyword, category, 1, 50).stream()
                        .map(ProductDTO::fromProduct)
                        .collect(Collectors.toList());
                resp.getWriter().write(JsonUtil.toJson(ApiResponse.success(list)));
            } else {
                Long productId = Long.parseLong(pathInfo.substring(1));
                Product product = productService.getProductById(productId);
                resp.getWriter().write(JsonUtil.toJson(ApiResponse.success(ProductDTO.fromProduct(product))));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.error(e.getMessage())));
        }
    }
}
