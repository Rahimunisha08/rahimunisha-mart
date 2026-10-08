package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.model.Order;
import com.rahimunisha.rahimunishamart.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Order createOrderWithItems(Order order, List<OrderItem> items);
    Optional<Order> findById(Long id);
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findBySellerId(Long sellerId);
    List<Order> findAll();
    boolean updateStatus(Long orderId, String newStatus);
    int countTotalOrders();
}
