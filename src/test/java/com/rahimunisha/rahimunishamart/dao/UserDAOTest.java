package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.dao.impl.UserDAOImpl;
import com.rahimunisha.rahimunishamart.model.Role;
import com.rahimunisha.rahimunishamart.model.User;
import com.rahimunisha.rahimunishamart.util.DatabaseUtil;
import com.rahimunisha.rahimunishamart.util.PasswordUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private static UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test_user_dao;DB_CLOSE_DELAY=-1;MODE=LEGACY");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(5);

        HikariDataSource ds = new HikariDataSource(config);
        DatabaseUtil.initDataSource(ds);
        DatabaseUtil.runSchemaAndMigrations();

        userDAO = new UserDAOImpl();
    }

    @AfterAll
    public static void tearDown() {
        DatabaseUtil.closeDataSource();
    }

    @Test
    public void testCreateAndFindUser() {
        User user = new User();
        user.setEmail("test.dao@example.com");
        user.setPasswordHash("hashed_pw_123");
        user.setFullName("Test DAO User");
        user.setRole(Role.BUYER);
        user.setPhone("9876543210");
        user.setAddress("Test Address");

        User created = userDAO.create(user);
        assertNotNull(created.getId());

        Optional<User> found = userDAO.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals("test.dao@example.com", found.get().getEmail());
        assertEquals(Role.BUYER, found.get().getRole());
    }

    @Test
    public void testFindByEmail() {
        Optional<User> user = userDAO.findByEmail("admin@rahimunishamart.com");
        assertTrue(user.isPresent());
        assertEquals(Role.ADMIN, user.get().getRole());
    }

    @Test
    public void testSeedAdminPassword() {
        Optional<User> user = userDAO.findByEmail("admin@rahimunishamart.com");
        assertTrue(user.isPresent());
        assertTrue(PasswordUtil.checkPassword("Password@123", user.get().getPasswordHash()), "Hash in seed should match Password@123");
    }
}
