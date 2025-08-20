package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    void saveOrderDish(List<OrderDish> orderDishes);
    boolean existsByCustomerIdAndStatusIn(Long customerId, List<String> statuses);
    PaginationResponse<Order> findByRestaurantIdAndStatus(Long restaurantId, String status, PaginationRequest paginationRequest);
    Optional<Order> findById(Long orderId);
}
