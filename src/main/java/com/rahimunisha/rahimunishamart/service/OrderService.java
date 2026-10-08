package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.CartDAO;
import com.rahimunisha.rahimunishamart.dao.OrderDAO;
import com.rahimunisha.rahimunishamart.dao.ProductDAO;
import com.rahimunisha.rahimunishamart.dao.impl.CartDAOImpl;
import com.rahimunisha.rahimunishamart.dao.impl.OrderDAOImpl;
import com.rahimunisha.rahimunishamart.dao.impl.ProductDAOImpl;
import com.rahimunisha.rahimunishamart.exception.ResourceNotFoundException;
import com.rahimunisha.rahimunishamart.exception.UnauthorizedException;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.CartItem;
import com.rahimunisha.rahimunishamart.model.Order;
import com.rahimunisha.rahimunishamart.model.OrderItem;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.model.Role;
import com.rahimunisha.rahimunishamart.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    private static final List<String> VALID_STATUSES = Arrays.asList(
            "PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"
    );

    public OrderService() {
        this.orderDAO = new OrderDAOImpl();
        this.cartDAO = new CartDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public Order checkoutCart(Long buyerId, String shippingAddress, String paymentMethod) {
        Map<String, String> errors = new HashMap<>();

        if (buyerId == null || buyerId <= 0) {
            errors.put("buyerId", "Invalid buyer session");
        }
        if (!ValidationUtil.isNonEmpty(shippingAddress)) {
            errors.put("shippingAddress", "Shipping address is mandatory for delivery");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Checkout validation failed", errors);
        }

        // 1. Retrieve cart items
        List<CartItem> cartItems = cartDAO.findByUserId(buyerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("cart", "Your shopping cart is empty. Please add items before checking out.");
        }

        BigDecimal orderTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 2. Validate stock and build order items
        for (CartItem ci : cartItems) {
            Product p = productDAO.findById(ci.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + ci.getProductId() + " no longer exists"));

            if (!"ACTIVE".equalsIgnoreCase(p.getStatus())) {
                throw new ValidationException("product", "Product '" + p.getName() + "' is currently inactive.");
            }

            if (p.getStockQty() < ci.getQuantity()) {
                throw new ValidationException("stock", "Product '" + p.getName() + "' has only " + p.getStockQty() + " items in stock (requested: " + ci.getQuantity() + ")");
            }

            OrderItem item = new OrderItem();
            item.setProductId(p.getId());
            item.setQuantity(ci.getQuantity());
            item.setUnitPrice(p.getPrice());
            orderItems.add(item);

            BigDecimal lineTotal = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            orderTotal = orderTotal.add(lineTotal);
        }

        // 3. Assemble Order entity
        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setTotalAmount(orderTotal);
        order.setStatus("CONFIRMED"); // Transition directly to CONFIRMED on mock payment
        order.setShippingAddress(shippingAddress.trim());
        order.setPaymentMethod(ValidationUtil.isNonEmpty(paymentMethod) ? paymentMethod.trim() : "MOCK_CARD");
        order.setPaymentStatus("PAID");

        // 4. Execute atomic transaction (order, order_items, decrement stock, clear cart)
        return orderDAO.createOrderWithItems(order, orderItems);
    }

    public Order getOrderDetails(Long orderId, Long requestingUserId, Role role) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Authorization check: Admins can see all; Buyers only see their own; Sellers see orders containing their products
        if (role == Role.ADMIN) {
            return order;
        }

        if (role == Role.BUYER && order.getBuyerId().equals(requestingUserId)) {
            return order;
        }

        if (role == Role.SELLER) {
            boolean hasSellerProduct = order.getItems().stream().anyMatch(item -> {
                Product p = productDAO.findById(item.getProductId()).orElse(null);
                return p != null && p.getSellerId().equals(requestingUserId);
            });
            if (hasSellerProduct) {
                return order;
            }
        }

        throw new UnauthorizedException("You are not authorized to view this order.");
    }

    public List<Order> getBuyerOrders(Long buyerId) {
        return orderDAO.findByBuyerId(buyerId);
    }

    public List<Order> getSellerOrders(Long sellerId) {
        return orderDAO.findBySellerId(sellerId);
    }

    public List<Order> getAllOrdersForAdmin() {
        return orderDAO.findAll();
    }

    public boolean updateOrderStatus(Long orderId, String newStatus, Long userId, Role role) {
        if (!VALID_STATUSES.contains(newStatus.toUpperCase())) {
            throw new ValidationException("status", "Invalid order status: " + newStatus);
        }

        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Access control:
        // Admin can update to any valid status
        // Seller can transition from CONFIRMED -> SHIPPED -> DELIVERED
        // Buyer can cancel if PENDING
        if (role == Role.ADMIN) {
            return orderDAO.updateStatus(orderId, newStatus.toUpperCase());
        }

        if (role == Role.SELLER) {
            // Verify this seller has items in this order
            boolean authorized = order.getItems().stream().anyMatch(item -> {
                Product p = productDAO.findById(item.getProductId()).orElse(null);
                return p != null && p.getSellerId().equals(userId);
            });
            if (!authorized) {
                throw new UnauthorizedException("You cannot update status for this order.");
            }
            return orderDAO.updateStatus(orderId, newStatus.toUpperCase());
        }

        if (role == Role.BUYER && order.getBuyerId().equals(userId)) {
            if ("CANCELLED".equalsIgnoreCase(newStatus) && "PENDING".equalsIgnoreCase(order.getStatus())) {
                return orderDAO.updateStatus(orderId, "CANCELLED");
            } else {
                throw new ValidationException("status", "Buyers can only cancel pending orders.");
            }
        }

        throw new UnauthorizedException("Unauthorized to modify order status.");
    }

    public int countTotalOrders() {
        return orderDAO.countTotalOrders();
    }
}
