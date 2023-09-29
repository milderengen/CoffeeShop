package com.example.orderservice;



import com.example.orderservice.ENUMS.OrderStatus;

import java.util.List;

public interface OrderRepo {
    Order save(Order order);
    Order findById(Long id);
    List<Order> findAllPendingOrders();
    List<Order> findAllOrders();
    void update(Order order);
    String delete(Order order);
}
