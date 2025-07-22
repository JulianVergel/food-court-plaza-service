package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;

import java.util.List;

public interface IOrderServicePort {
    void createOrder(Order order, List<OrderDish> orderDishes);
}
