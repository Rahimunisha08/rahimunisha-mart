package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home", "/browse"})
public class HomeServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("q");
        String category = req.getParameter("category");
        String pageStr = req.getParameter("page");

        int page = 1;
        int pageSize = 8;
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException ignored) {
            }
        }

        List<Product> products = productService.getProducts(keyword, category, page, pageSize);
        int totalProducts = productService.countProducts(keyword, category);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        List<String> categories = productService.getCategories();

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("currentCategory", category);
        req.setAttribute("currentKeyword", keyword);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalProducts);

        req.getRequestDispatcher("/WEB-INF/views/buyer/home.jsp").forward(req, resp);
    }
}
