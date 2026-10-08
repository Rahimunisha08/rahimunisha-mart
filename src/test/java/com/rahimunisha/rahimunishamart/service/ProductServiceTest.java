package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.ProductDAO;
import com.rahimunisha.rahimunishamart.exception.UnauthorizedException;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDAO productDAO;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = new ProductService(productDAO);
    }

    @Test
    public void testCreateProductSuccess() {
        when(productDAO.create(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(101L);
            return p;
        });

        Product created = productService.createProduct(
                2L, "Gaming Headset", "Surround 7.1", "Electronics",
                new BigDecimal("1999.00"), 10, "https://example.com/headset.jpg"
        );

        assertNotNull(created);
        assertEquals(101L, created.getId());
        assertEquals("Gaming Headset", created.getName());
        verify(productDAO, times(1)).create(any(Product.class));
    }

    @Test
    public void testCreateProductRejectsNegativePrice() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
                productService.createProduct(
                        2L, "Invalid Product", "Desc", "Electronics",
                        new BigDecimal("-50.00"), 10, null
                )
        );
        assertTrue(ex.getFieldErrors().containsKey("price"));
        verify(productDAO, never()).create(any(Product.class));
    }

    @Test
    public void testUpdateProductUnauthorizedSeller() {
        Product existing = new Product();
        existing.setId(5L);
        existing.setSellerId(99L); // Owned by Seller 99

        when(productDAO.findById(5L)).thenReturn(Optional.of(existing));

        // Seller 2 attempts to modify Seller 99's product
        assertThrows(UnauthorizedException.class, () ->
                productService.updateProduct(
                        2L, 5L, "Renamed Product", "New Desc", "Fashion",
                        new BigDecimal("899.00"), 5, null, "ACTIVE"
                )
        );

        verify(productDAO, never()).update(any(Product.class));
    }
}
