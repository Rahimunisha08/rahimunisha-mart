package com.rahimunisha.rahimunishamart.filter;

import com.rahimunisha.rahimunishamart.model.Role;
import com.rahimunisha.rahimunishamart.model.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthFilter: Enforces role-based access control and session verification across protected endpoints.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Check if path is protected
        boolean isSellerPath = path.startsWith("/seller");
        boolean isAdminPath = path.startsWith("/admin");
        boolean isBuyerProtectedPath = path.startsWith("/checkout") || path.startsWith("/orders");

        if (!isSellerPath && !isAdminPath && !isBuyerProtectedPath) {
            // Public path (browse, product details, auth, static assets, health, chat, cart, wishlist)
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        // Unauthenticated access attempt
        if (currentUser == null) {
            if (path.startsWith("/api/")) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\":false,\"error\":\"Authentication required. Please log in.\"}");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        // Role-based authorization
        if (isAdminPath && currentUser.getRole() != Role.ADMIN) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Administrator privileges required.");
            return;
        }

        if (isSellerPath && currentUser.getRole() != Role.SELLER && currentUser.getRole() != Role.ADMIN) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Seller privileges required.");
            return;
        }

        // Authenticated and authorized
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
