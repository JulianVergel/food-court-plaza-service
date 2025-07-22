package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    void saveOrderDish(List<OrderDish> orderDishes);
    boolean existsByCustomerIdAndStatusIn(Long customerId, List<String> statuses);
    Page<Order> findByRestaurantIdAndStatus(Long restaurantId, String status, Pageable pageable);
    Optional<Order> findById(Long orderId);
}
