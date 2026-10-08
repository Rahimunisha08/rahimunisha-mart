package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.dao.impl.CartDAOImpl;
import com.rahimunisha.rahimunishamart.dao.impl.OrderDAOImpl;
import com.rahimunisha.rahimunishamart.dao.impl.ProductDAOImpl;
import com.rahimunisha.rahimunishamart.model.Order;
import com.rahimunisha.rahimunishamart.model.OrderItem;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.util.DatabaseUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDAOTest {
    private static OrderDAO orderDAO;
    private static ProductDAO productDAO;
    private static CartDAO cartDAO;

    @BeforeAll
    public static void setUp() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test_order_dao;DB_CLOSE_DELAY=-1;MODE=LEGACY");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(5);

        HikariDataSource ds = new HikariDataSource(config);
        DatabaseUtil.initDataSource(ds);
        DatabaseUtil.runSchemaAndMigrations();

        orderDAO = new OrderDAOImpl();
        productDAO = new ProductDAOImpl();
        cartDAO = new CartDAOImpl();
    }

    @AfterAll
    public static void tearDown() {
        DatabaseUtil.closeDataSource();
    }

    @Test
    public void testCreateOrderTransactionalFlow() {
        // Find existing product #1
        Optional<Product> prodOpt = productDAO.findById(1L);
        assertTrue(prodOpt.isPresent());
        int initialStock = prodOpt.get().getStockQty();

        // Put item in buyer #4 cart
        cartDAO.addToCart(4L, 1L, 2);

        Order order = new Order();
        order.setBuyerId(4L);
        order.setTotalAmount(new BigDecimal("9998.00"));
        order.setStatus("CONFIRMED");
        order.setShippingAddress("Test Street 100, Chennai");

        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("4999.00"));

        Order placed = orderDAO.createOrderWithItems(order, Collections.singletonList(item));
        assertNotNull(placed.getId());

        // Verify stock decremented by 2
        Optional<Product> updatedProd = productDAO.findById(1L);
        assertTrue(updatedProd.isPresent());
        assertEquals(initialStock - 2, updatedProd.get().getStockQty());

        // Verify cart cleared for buyer #4
        assertEquals(0, cartDAO.getCartCount(4L));
    }
}
