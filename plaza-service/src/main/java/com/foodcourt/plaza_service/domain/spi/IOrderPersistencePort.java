package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import java.util.List;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    void saveOrderDish(List<OrderDish> orderDishes);
    boolean existsByCustomerIdAndStatusIn(Long customerId, List<String> statuses);
}
