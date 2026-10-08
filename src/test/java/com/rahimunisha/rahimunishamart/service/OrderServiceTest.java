package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.CartDAO;
import com.rahimunisha.rahimunishamart.dao.OrderDAO;
import com.rahimunisha.rahimunishamart.dao.ProductDAO;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.CartItem;
import com.rahimunisha.rahimunishamart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ProductDAO productDAO;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderDAO, cartDAO, productDAO);
    }

    @Test
    public void testCheckoutEmptyCartFails() {
        when(cartDAO.findByUserId(10L)).thenReturn(Collections.emptyList());

        ValidationException ex = assertThrows(ValidationException.class, () ->
                orderService.checkoutCart(10L, "123 Main St, Chennai", "MOCK_CARD")
        );

        assertTrue(ex.getMessage().contains("empty"));
        verify(orderDAO, never()).createOrderWithItems(any(), any());
    }

    @Test
    public void testCheckoutOutOfStockFails() {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setQuantity(5);

        Product product = new Product();
        product.setId(1L);
        product.setName("Limited Item");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockQty(2); // Only 2 in stock, cart asks for 5
        product.setStatus("ACTIVE");

        when(cartDAO.findByUserId(10L)).thenReturn(Collections.singletonList(cartItem));
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));

        ValidationException ex = assertThrows(ValidationException.class, () ->
                orderService.checkoutCart(10L, "123 Main St, Chennai", "MOCK_CARD")
        );

        assertTrue(ex.getMessage().contains("stock") || ex.getFieldErrors().containsKey("stock"));
        verify(orderDAO, never()).createOrderWithItems(any(), any());
    }
}
