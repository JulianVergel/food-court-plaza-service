package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.api.IOrderServicePort;
import com.foodcourt.plaza_service.domain.exception.ClientHasAnOrderException;
import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import com.foodcourt.plaza_service.domain.model.Traceability;
import com.foodcourt.plaza_service.domain.spi.IOrderPersistencePort;
import com.foodcourt.plaza_service.domain.spi.ITraceabilityPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserContextProviderPort userContextProviderPort;
    private final ITraceabilityPersistencePort traceabilityPersistencePort;

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
}
