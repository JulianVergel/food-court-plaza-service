package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.api.IOrderServicePort;
import com.foodcourt.plaza_service.domain.exception.ClientHasAnOrderException;
import com.foodcourt.plaza_service.domain.exception.NotRestaurantOwnerException;
import com.foodcourt.plaza_service.domain.exception.OrderCannotBeAssignedException;
import com.foodcourt.plaza_service.domain.exception.OrderNotFoundException;
import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import com.foodcourt.plaza_service.domain.model.Traceability;
import com.foodcourt.plaza_service.domain.spi.IEmployeePersistencePort;
import com.foodcourt.plaza_service.domain.spi.IOrderPersistencePort;
import com.foodcourt.plaza_service.domain.spi.ITraceabilityPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserContextProviderPort userContextProviderPort;
    private final ITraceabilityPersistencePort traceabilityPersistencePort;
    private final IEmployeePersistencePort employeePersistencePort;

    @Override
    public void createOrder(Order order, List<OrderDish> orderDishes) {
        Long customerId = userContextProviderPort.getAuthenticatedUserId();
        String customerEmail = userContextProviderPort.getAuthenticatedUserEmail();

        List<String> statuses = Arrays.asList("PENDIENTE", "EN_PREPARACION", "LISTO");
        if (orderPersistencePort.existsByCustomerIdAndStatusIn(customerId, statuses)) {
            throw new ClientHasAnOrderException();
        }

        order.setCustomerId(customerId);
        order.setOrderDate(LocalDate.now());
        order.setStatus("PENDIENTE");

        Order savedOrder = orderPersistencePort.saveOrder(order);

        orderDishes.forEach(od -> od.setOrderId(savedOrder.getId()));
        orderPersistencePort.saveOrderDish(orderDishes);

        Traceability trace = new Traceability(
                savedOrder.getId(),
                customerId,
                customerEmail,
                LocalDateTime.now(),
                null, // No hay estado previo
                "PENDIENTE",
                null,
                null
        );
        traceabilityPersistencePort.logOrderTrace(trace);
    }

    @Override
    public Page<Order> listOrdersByStatus(String status, int page, int size) {
        Long employeeId = userContextProviderPort.getAuthenticatedUserId();

        Long restaurantId = employeePersistencePort.findRestaurantIdByEmployeeId(employeeId);

        Pageable pageable = PageRequest.of(page, size);

        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, pageable);
    }

    @Override
    public void assignOrderToEmployee(Long orderId) {
        Long employeeId = userContextProviderPort.getAuthenticatedUserId();
        String employeeEmail = userContextProviderPort.getAuthenticatedUserEmail();

        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!"PENDIENTE".equalsIgnoreCase(order.getStatus())) {
            throw new OrderCannotBeAssignedException();
        }

        Long employeeRestaurantId = employeePersistencePort.findRestaurantIdByEmployeeId(employeeId);
        if (!employeeRestaurantId.equals(order.getRestaurantId())) {
            throw new NotRestaurantOwnerException();
        }

        order.setChefId(employeeId);
        order.setStatus("EN_PREPARACION");
        orderPersistencePort.saveOrder(order);

        Traceability trace = new Traceability(
                order.getId(),
                order.getCustomerId(),
                null,
                LocalDateTime.now(),
                "PENDIENTE",
                "EN_PREPARACION",
                employeeId,
                employeeEmail
        );
        traceabilityPersistencePort.logOrderTrace(trace);
    }
}
