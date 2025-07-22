package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderServicePort {
    void createOrder(Order order, List<OrderDish> orderDishes);
    Page<Order> listOrdersByStatus(String status, int page, int size);
}
