package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.dao.impl.ProductDAOImpl;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.util.DatabaseUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDAOTest {
    private static ProductDAO productDAO;

    @BeforeAll
    public static void setUp() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test_prod_dao;DB_CLOSE_DELAY=-1;MODE=LEGACY");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(5);

        HikariDataSource ds = new HikariDataSource(config);
        DatabaseUtil.initDataSource(ds);
        DatabaseUtil.runSchemaAndMigrations();

        productDAO = new ProductDAOImpl();
    }

    @AfterAll
    public static void tearDown() {
        DatabaseUtil.closeDataSource();
    }

    @Test
    public void testFindAllActiveAndFilter() {
        List<Product> products = productDAO.findAllActive(null, null, 0, 10);
        assertFalse(products.isEmpty());

        List<Product> electronics = productDAO.findAllActive(null, "Electronics", 0, 10);
        assertFalse(electronics.isEmpty());
        for (Product p : electronics) {
            assertEquals("Electronics", p.getCategory());
        }
    }

    @Test
    public void testCreateProduct() {
        Product p = new Product();
        p.setSellerId(2L);
        p.setName("Wireless Mouse X");
        p.setDescription("Ergonomic 2.4G optical mouse");
        p.setCategory("Electronics");
        p.setPrice(new BigDecimal("799.00"));
        p.setStockQty(25);
        p.setImageUrl("https://example.com/mouse.jpg");
        p.setStatus("ACTIVE");

        Product created = productDAO.create(p);
        assertNotNull(created.getId());

        Optional<Product> found = productDAO.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals("Wireless Mouse X", found.get().getName());
        assertEquals(0, new BigDecimal("799.00").compareTo(found.get().getPrice()));
    }
}
