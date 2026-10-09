package com.rahimunisha.rahimunishamart.controller;

import com.rahimunisha.rahimunishamart.dto.LoginRequestDTO;
import com.rahimunisha.rahimunishamart.dto.RegisterRequestDTO;
import com.rahimunisha.rahimunishamart.exception.AuthenticationException;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.Role;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth/login", "/auth/register", "/auth/logout"})
public class AuthServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/auth/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/auth/login?logout=true");
            return;
        }

        if ("/auth/login".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        if ("/auth/register".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/auth/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/auth/register".equals(path)) {
            handleRegister(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");

        LoginRequestDTO loginReq = new LoginRequestDTO(email, password);

        try {
            User user = userService.login(loginReq);

            // Standing Rule: Regenerate session ID on login to protect against session fixation attacks
            HttpSession session = req.getSession(true);
            try {
                req.changeSessionId();
            } catch (Exception ignored) {
            }
            session.setAttribute("currentUser", user);

            if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("/auth/")) {
                resp.sendRedirect(redirect);
                return;
            }

            if (user.getRole() == Role.ADMIN) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else if (user.getRole() == Role.SELLER) {
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }

        } catch (AuthenticationException | ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("enteredEmail", email);
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        RegisterRequestDTO regReq = new RegisterRequestDTO(email, password, fullName, role, phone, address);

        try {
            userService.register(regReq);
            req.setAttribute("successMessage", "Account created successfully! Please log in.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        } catch (ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("fieldErrors", e.getFieldErrors());
            req.setAttribute("enteredFullName", fullName);
            req.setAttribute("enteredEmail", email);
            req.setAttribute("enteredRole", role);
            req.setAttribute("enteredPhone", phone);
            req.setAttribute("enteredAddress", address);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }
}
