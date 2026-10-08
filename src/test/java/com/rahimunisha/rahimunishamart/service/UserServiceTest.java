package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.UserDAO;
import com.rahimunisha.rahimunishamart.dto.LoginRequestDTO;
import com.rahimunisha.rahimunishamart.dto.RegisterRequestDTO;
import com.rahimunisha.rahimunishamart.dto.UserResponseDTO;
import com.rahimunisha.rahimunishamart.exception.AuthenticationException;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.Role;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userDAO);
    }

    @Test
    public void testRegisterSuccess() {
        RegisterRequestDTO req = new RegisterRequestDTO(
                "newbuyer@example.com", "SecurePass123", "New Buyer", "BUYER", "9876543210", "Chennai"
        );

        when(userDAO.findByEmail("newbuyer@example.com")).thenReturn(Optional.empty());
        when(userDAO.create(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(99L);
            return u;
        });

        UserResponseDTO response = userService.register(req);

        assertNotNull(response);
        assertEquals(99L, response.getId());
        assertEquals("newbuyer@example.com", response.getEmail());
        assertEquals("New Buyer", response.getFullName());
        assertEquals(Role.BUYER, response.getRole());
        verify(userDAO, times(1)).create(any(User.class));
    }

    @Test
    public void testRegisterRejectsAdminRole() {
        RegisterRequestDTO req = new RegisterRequestDTO(
                "hacker@example.com", "Password123", "Hacker", "ADMIN", "9876543210", "Nowhere"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> userService.register(req));
        assertTrue(ex.getFieldErrors().containsKey("role"));
        verify(userDAO, never()).create(any(User.class));
    }

    @Test
    public void testRegisterRejectsInvalidEmail() {
        RegisterRequestDTO req = new RegisterRequestDTO(
                "not-an-email", "Password123", "Test User", "BUYER", null, null
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> userService.register(req));
        assertTrue(ex.getFieldErrors().containsKey("email"));
        verify(userDAO, never()).create(any(User.class));
    }

    @Test
    public void testLoginSuccess() {
        String hashed = PasswordUtil.hashPassword("Secret123");
        User user = new User();
        user.setId(10L);
        user.setEmail("user@example.com");
        user.setPasswordHash(hashed);
        user.setRole(Role.BUYER);

        when(userDAO.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        LoginRequestDTO req = new LoginRequestDTO("user@example.com", "Secret123");
        User loggedIn = userService.login(req);

        assertNotNull(loggedIn);
        assertEquals(10L, loggedIn.getId());
    }

    @Test
    public void testLoginWrongPassword() {
        String hashed = PasswordUtil.hashPassword("Secret123");
        User user = new User();
        user.setEmail("user@example.com");
        user.setPasswordHash(hashed);

        when(userDAO.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        LoginRequestDTO req = new LoginRequestDTO("user@example.com", "WrongPassword");
        assertThrows(AuthenticationException.class, () -> userService.login(req));
    }
}
