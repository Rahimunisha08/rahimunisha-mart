package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.util.DatabaseUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HealthServlet: System health verification endpoint.
 * Requirement: Verify GET /api/v1/health returns {"status":"UP","db":"UP"}.
 */
@WebServlet(name = "HealthServlet", urlPatterns = {"/api/v1/health", "/health"})
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        boolean dbHealthy = DatabaseUtil.isHealthy();
        if (dbHealthy) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"UP\",\"db\":\"UP\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().write("{\"status\":\"DOWN\",\"db\":\"DOWN\"}");
        }
    }
}
