package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;

import java.util.List;

public interface IOrderServicePort {
    void createOrder(Order order, List<OrderDish> orderDishes);
    PaginationResponse<Order> listOrdersByStatus(String status, int page, int size);
    void assignOrderToEmployee(Long orderId);
    void notifyOrderReady(Long orderId);
    void deliverOrder(Long orderId, String pin);
    void cancelOrder(Long orderId);
}
