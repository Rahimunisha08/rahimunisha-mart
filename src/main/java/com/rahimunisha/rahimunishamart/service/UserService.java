package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.UserDAO;
import com.rahimunisha.rahimunishamart.dao.impl.UserDAOImpl;
import com.rahimunisha.rahimunishamart.dto.LoginRequestDTO;
import com.rahimunisha.rahimunishamart.dto.RegisterRequestDTO;
import com.rahimunisha.rahimunishamart.dto.UserResponseDTO;
import com.rahimunisha.rahimunishamart.exception.AuthenticationException;
import com.rahimunisha.rahimunishamart.exception.ResourceNotFoundException;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.Role;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.util.PasswordUtil;
import com.rahimunisha.rahimunishamart.util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponseDTO register(RegisterRequestDTO req) {
        Map<String, String> errors = new HashMap<>();

        if (req == null) {
            throw new ValidationException("Registration request cannot be null");
        }

        if (!ValidationUtil.isNonEmpty(req.getFullName())) {
            errors.put("fullName", "Full name is required");
        }

        if (!ValidationUtil.isValidEmail(req.getEmail())) {
            errors.put("email", "A valid email address is required");
        }

        if (!ValidationUtil.isValidPassword(req.getPassword())) {
            errors.put("password", "Password must be at least 6 characters long");
        }

        Role requestedRole = Role.fromString(req.getRole());
        // F1 Rule: Admin is a seed account only — no admin signup flow!
        if (requestedRole == Role.ADMIN) {
            errors.put("role", "Administrator registration is disabled. Admin accounts are seeded only.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed for user registration", errors);
        }

        // Check if email already taken
        if (userDAO.findByEmail(req.getEmail().trim()).isPresent()) {
            errors.put("email", "Email address is already registered");
            throw new ValidationException("Duplicate email", errors);
        }

        String hashedPassword = PasswordUtil.hashPassword(req.getPassword().trim());
        User user = new User();
        user.setEmail(req.getEmail().trim().toLowerCase());
        user.setPasswordHash(hashedPassword);
        user.setFullName(req.getFullName().trim());
        user.setRole(requestedRole);
        user.setPhone(req.getPhone() != null ? req.getPhone().trim() : null);
        user.setAddress(req.getAddress() != null ? req.getAddress().trim() : null);

        User saved = userDAO.create(user);
        return UserResponseDTO.fromUser(saved);
    }

    public User login(LoginRequestDTO req) {
        Map<String, String> errors = new HashMap<>();
        if (req == null) {
            throw new ValidationException("Login request cannot be null");
        }
        if (!ValidationUtil.isValidEmail(req.getEmail())) {
            errors.put("email", "Valid email address is required");
        }
        if (!ValidationUtil.isNonEmpty(req.getPassword())) {
            errors.put("password", "Password cannot be empty");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed for login", errors);
        }

        User user = userDAO.findByEmail(req.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password."));

        if (!PasswordUtil.checkPassword(req.getPassword().trim(), user.getPasswordHash())) {
            throw new AuthenticationException("Invalid email or password.");
        }

        return user;
    }

    public UserResponseDTO getUserProfile(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("id", "Invalid user ID");
        }
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserResponseDTO.fromUser(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(UserResponseDTO::fromUser)
                .collect(Collectors.toList());
    }

    public boolean deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("id", "Invalid user ID");
        }
        return userDAO.delete(id);
    }
}
