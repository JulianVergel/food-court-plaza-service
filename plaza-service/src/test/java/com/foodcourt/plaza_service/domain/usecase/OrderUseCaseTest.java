package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.exception.*;
import com.foodcourt.plaza_service.domain.model.*;
import com.foodcourt.plaza_service.domain.spi.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IUserContextProviderPort userContextProviderPort;
    @Mock
    private ITraceabilityPersistencePort traceabilityPersistencePort;
    @Mock
    private IEmployeePersistencePort employeePersistencePort;
    @Mock
    private IMessagingPersistencePort messagingPersistencePort;
    @Mock
    private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order(1L, 5L, LocalDate.now(), "PENDIENTE", null, 31L, null);
    }

    @Test
    void testCreateOrder_Success() {
        Long customerId = 10L;
        String customerEmail = "cliente@example.com";
        Order orderToSave = new Order(null, null, null, null, null, 5L, null);
        Order savedOrder = new Order(1L, customerId, null, "PENDIENTE", null, 5L, null);
        List<OrderDish> dishes = Collections.singletonList(new OrderDish(null, 1L, 2));

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(customerId);
        when(userContextProviderPort.getAuthenticatedUserEmail()).thenReturn(customerEmail);
        when(orderPersistencePort.existsByCustomerIdAndStatusIn(eq(customerId), anyList())).thenReturn(false);
        when(orderPersistencePort.saveOrder(any(Order.class))).thenReturn(savedOrder);

        orderUseCase.createOrder(orderToSave, dishes);

        verify(orderPersistencePort).saveOrder(any(Order.class));
        verify(orderPersistencePort).saveOrderDish(anyList());
        ArgumentCaptor<Traceability> traceCaptor = ArgumentCaptor.forClass(Traceability.class);
        verify(traceabilityPersistencePort).logOrderTrace(traceCaptor.capture());
        assertEquals("PENDIENTE", traceCaptor.getValue().getNewStatus());
    }

    @Test
    void testAssignOrderToEmployee_Success() {
        Long orderId = 1L;
        Long employeeId = 66L;
        Long restaurantId = 31L;
        testOrder.setStatus("PENDIENTE");
        testOrder.setRestaurantId(restaurantId);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(restaurantId);

        orderUseCase.assignOrderToEmployee(orderId);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals("EN_PREPARACION", capturedOrder.getStatus());
        assertEquals(employeeId, capturedOrder.getChefId());
    }

    @Test
    void testAssignOrderToEmployee_FailsWhenOrderIsNotPending() {
        Long orderId = 1L;
        testOrder.setStatus("EN_PREPARACION");

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(66L);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(66L)).thenReturn(testOrder.getRestaurantId());

        assertThrows(OrderCannotBeAssignedException.class, () -> orderUseCase.assignOrderToEmployee(orderId));
        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    void testNotifyOrderReady_Success() {
        Long orderId = 1L;
        Long employeeId = 66L;
        Long customerId = 5L;
        Long restaurantId = 31L;
        String customerPhone = "+573001234567";
        testOrder.setStatus("EN_PREPARACION");
        testOrder.setRestaurantId(restaurantId);
        testOrder.setCustomerId(customerId);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(restaurantId);
        when(userPersistencePort.findUserPhoneById(customerId)).thenReturn(customerPhone);

        orderUseCase.notifyOrderReady(orderId);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());
        assertEquals("LISTO", orderCaptor.getValue().getStatus());
        assertNotNull(orderCaptor.getValue().getSecurityPin());

        verify(messagingPersistencePort, times(1)).sendNotification(eq(customerPhone), anyString());
    }

    @Test
    void testNotifyOrderReady_FailsWhenOrderIsNotInPreparation() {
        Long orderId = 1L;
        testOrder.setStatus("PENDIENTE"); // Set to a non-preparation status

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(66L);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(66L)).thenReturn(testOrder.getRestaurantId());

        assertThrows(OrderIsNotInPreparationException.class, () -> orderUseCase.notifyOrderReady(orderId));
        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    void testDeliverOrder_FailsWhenPinIsInvalid() {
        // Arrange
        Long orderId = 1L;
        String correctPin = "1234";
        String wrongPin = "9999";
        testOrder.setStatus("LISTO");
        testOrder.setSecurityPin(correctPin);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(66L);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(66L)).thenReturn(testOrder.getRestaurantId());

        assertThrows(InvalidPinException.class, () -> orderUseCase.deliverOrder(orderId, wrongPin));
        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    void testDeliverOrder_FailsWhenOrderIsNotReady() {
        Long orderId = 1L;
        testOrder.setStatus("EN_PREPARACION");

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(66L);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(66L)).thenReturn(testOrder.getRestaurantId());

        assertThrows(OrderIsNotReadyException.class, () -> orderUseCase.deliverOrder(orderId, "1234"));
        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    void testCreateOrder_FailsWhenOrderIsNull() {
        assertThrows(NullPointerException.class, () -> orderUseCase.createOrder(null, Collections.emptyList()));
    }

    @Test
    void testAssignOrder_FailsWhenOrderNotFound() {
        Long orderId = 999L;
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderUseCase.assignOrderToEmployee(orderId));
    }

    @Test
    void testDeliverOrder_FailsWhenEmployeeDoesNotBelongToRestaurant() {
        Long orderId = 1L;
        Long employeeId = 66L;
        Long orderRestaurantId = 31L;
        Long employeeRestaurantId = 99L;
        Order readyOrder = new Order(orderId, 5L, LocalDate.now(), "LISTO", employeeId, orderRestaurantId, "1234");

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(readyOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(employeeRestaurantId);

        assertThrows(NotRestaurantOwnerException.class, () -> orderUseCase.deliverOrder(orderId, "1234"));
    }

    @Test
    void testCancelOrder_Success() {
        Long orderId = 1L;
        Long customerId = 5L;
        Order pendingOrder = new Order(orderId, customerId, LocalDate.now(), "PENDIENTE", null, 31L, null);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(customerId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(pendingOrder));

        orderUseCase.cancelOrder(orderId);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());
        assertEquals("CANCELADO", orderCaptor.getValue().getStatus());
    }

    @Test
    void testDeliverOrder_Success() {
        Long orderId = 1L;
        Long employeeId = 66L;
        Long restaurantId = 31L;
        String correctPin = "1234";
        Order readyOrder = new Order(orderId, 5L, LocalDate.now(), "LISTO", employeeId, restaurantId, correctPin);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(readyOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(restaurantId);

        orderUseCase.deliverOrder(orderId, correctPin);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());
        assertEquals("ENTREGADO", orderCaptor.getValue().getStatus());
    }

    @Test
    void testCancelOrder_FailsWhenUserIsNotOrderOwner() {
        Long orderId = 1L;
        Long orderCustomerId = 5L;
        Long differentCustomerId = 99L; // Un cliente diferente
        Order pendingOrder = new Order(orderId, orderCustomerId, LocalDate.now(), "PENDIENTE", null, 31L, null);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(differentCustomerId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(pendingOrder));

        assertThrows(UserCanNotCancelOrderException.class, () -> orderUseCase.cancelOrder(orderId));
    }

    @Test
    void testCancelOrder_FailsWhenOrderIsNotPending() {
        Long orderId = 1L;
        Long customerId = 5L;
        Order readyOrder = new Order(orderId, customerId, LocalDate.now(), "LISTO", 66L, 31L, "1234");

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(customerId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(readyOrder));

        assertThrows(OrderCannotBeCanceledException.class, () -> orderUseCase.cancelOrder(orderId));
    }

    @Test
    void testCreateOrder_FailsWhenClientHasAnActiveOrder() {
        Long customerId = 10L;
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(customerId);
        when(orderPersistencePort.existsByCustomerIdAndStatusIn(eq(customerId), anyList())).thenReturn(true);

        assertThrows(ClientHasAnOrderException.class, () -> {
            orderUseCase.createOrder(new Order(), Collections.emptyList());
        });
    }

    @Test
    void testListOrdersByStatus_Success() {
        String status = "PENDIENTE";
        int page = 0;
        int size = 10;
        Long employeeId = 66L;
        Long restaurantId = 31L;
        PaginationResponse<Order> expectedPage = new PaginationResponse<>(Collections.emptyList(), 0L, 0, page, size);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findByRestaurantIdAndStatus(eq(restaurantId), eq(status), any(PaginationRequest.class)))
                .thenReturn(expectedPage);

        PaginationResponse<Order> result = orderUseCase.listOrdersByStatus(status, page, size);

        verify(employeePersistencePort, times(1)).findRestaurantIdByEmployeeId(employeeId);
        verify(orderPersistencePort, times(1)).findByRestaurantIdAndStatus(eq(restaurantId), eq(status), any(PaginationRequest.class));
        assertEquals(expectedPage, result);
    }
}