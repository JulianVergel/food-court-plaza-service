package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.api.IOrderServicePort;
import com.foodcourt.plaza_service.domain.exception.ClientHasAnOrderException;
import com.foodcourt.plaza_service.domain.model.*;
import com.foodcourt.plaza_service.domain.spi.IOrderPersistencePort;
import com.foodcourt.plaza_service.domain.spi.ITraceabilityPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import com.foodcourt.plaza_service.domain.exception.*;
import com.foodcourt.plaza_service.domain.spi.*;
import com.foodcourt.plaza_service.domain.utils.constants.DomainConstants;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {

    private static final Random RANDOM = new Random();

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserContextProviderPort userContextProviderPort;
    private final ITraceabilityPersistencePort traceabilityPersistencePort;
    private final IEmployeePersistencePort employeePersistencePort;
    private final IMessagingPersistencePort messagingPersistencePort;
    private final IUserPersistencePort userPersistencePort;

    @Override
    public void createOrder(Order order, List<OrderDish> orderDishes) {
        Long customerId = userContextProviderPort.getAuthenticatedUserId();

        List<String> statuses = Arrays.asList(DomainConstants.STATUS_PENDING,
                DomainConstants.STATUS_IN_PREPARATION, DomainConstants.STATUS_READY);
        if (orderPersistencePort.existsByCustomerIdAndStatusIn(customerId, statuses)) {
            throw new ClientHasAnOrderException();
        }

        order.setCustomerId(customerId);
        order.setOrderDate(LocalDate.now());
        order.setStatus(DomainConstants.STATUS_PENDING);

        Order savedOrder = orderPersistencePort.saveOrder(order);

        orderDishes.forEach(od -> od.setOrderId(savedOrder.getId()));
        orderPersistencePort.saveOrderDish(orderDishes);

        logTrace(savedOrder, null, savedOrder.getStatus());
    }

    @Override
    public Page<Order> listOrdersByStatus(String status, int page, int size) {
        Long employeeId = userContextProviderPort.getAuthenticatedUserId();
        Long restaurantId = employeePersistencePort.findRestaurantIdByEmployeeId(employeeId);
        PaginationRequest paginationRequest = new PaginationRequest(page, size);
        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, paginationRequest);
    }

    @Override
    public void assignOrderToEmployee(Long orderId) {
        Order order = findOrderAndValidateOwnership(orderId);

        if (!DomainConstants.STATUS_PENDING.equalsIgnoreCase(order.getStatus())) {
            throw new OrderCannotBeAssignedException();
        }

        String previousStatus = order.getStatus();
        order.setChefId(userContextProviderPort.getAuthenticatedUserId());
        order.setStatus(DomainConstants.STATUS_IN_PREPARATION);
        orderPersistencePort.saveOrder(order);

        logTrace(order, previousStatus, order.getStatus());
    }

    @Override
    public void notifyOrderReady(Long orderId) {
        Order order = findOrderAndValidateOwnership(orderId);

        if (!DomainConstants.STATUS_IN_PREPARATION.equalsIgnoreCase(order.getStatus())) {
            throw new OrderIsNotInPreparationException();
        }

        String pin = String.format(DomainConstants.PIN_FORMAT, RANDOM.nextInt(DomainConstants.PIN_SIZE));
        String previousStatus = order.getStatus();

        order.setStatus(DomainConstants.STATUS_READY);
        order.setSecurityPin(pin);
        orderPersistencePort.saveOrder(order);

        String customerPhone = userPersistencePort.findUserPhoneById(order.getCustomerId());
        messagingPersistencePort.sendNotification(customerPhone, DomainConstants.ORDER_READY_MESSAGE + pin);

        logTrace(order, previousStatus, order.getStatus());
    }

    @Override
    public void deliverOrder(Long orderId, String pin) {
        Order order = findOrderAndValidateOwnership(orderId);

        if (!DomainConstants.STATUS_READY.equalsIgnoreCase(order.getStatus())) {
            throw new OrderIsNotReadyException();
        }
        if (!order.getSecurityPin().equals(pin)) {
            throw new InvalidPinException();
        }

        String previousStatus = order.getStatus();
        order.setStatus(DomainConstants.STATUS_DELIVERED);
        orderPersistencePort.saveOrder(order);

        logTrace(order, previousStatus, order.getStatus());
    }

    @Override
    public void cancelOrder(Long orderId) {
        Long customerId = userContextProviderPort.getAuthenticatedUserId();
        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getCustomerId().equals(customerId)) {
            throw new UserCanNotCancelOrderException();
        }
        if (!DomainConstants.STATUS_PENDING.equalsIgnoreCase(order.getStatus())) {
            throw new OrderCannotBeCanceledException();
        }

        String previousStatus = order.getStatus();
        order.setStatus(DomainConstants.STATUS_CANCELED);
        orderPersistencePort.saveOrder(order);

        logTrace(order, previousStatus, order.getStatus());
    }

    private Order findOrderAndValidateOwnership(Long orderId) {
        Long employeeId = userContextProviderPort.getAuthenticatedUserId();
        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        Long employeeRestaurantId = employeePersistencePort.findRestaurantIdByEmployeeId(employeeId);
        if (!employeeRestaurantId.equals(order.getRestaurantId())) {
            throw new NotRestaurantOwnerException();
        }
        return order;
    }

    private void logTrace(Order order, String previousStatus, String newStatus) {
        Long customerId = order.getCustomerId();
        Long employeeId = userContextProviderPort.getAuthenticatedUserId(); // Asumimos que el empleado es el que actúa
        String customerEmail = null;
        String employeeEmail = null;

        if (newStatus.equals(DomainConstants.STATUS_PENDING) || newStatus.equals(DomainConstants.STATUS_CANCELED)) {
            employeeId = null; // No hay empleado en estas acciones
            customerEmail = userContextProviderPort.getAuthenticatedUserEmail();
        } else {
            employeeEmail = userContextProviderPort.getAuthenticatedUserEmail();
        }

        Traceability trace = new Traceability(
                order.getId(),
                customerId,
                customerEmail,
                LocalDateTime.now(),
                previousStatus,
                newStatus,
                employeeId,
                employeeEmail,
                order.getRestaurantId()
        );
        traceabilityPersistencePort.logOrderTrace(trace);
    }
}
