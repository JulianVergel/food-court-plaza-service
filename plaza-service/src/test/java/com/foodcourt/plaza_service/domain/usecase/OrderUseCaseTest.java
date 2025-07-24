package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.exception.*;
import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import com.foodcourt.plaza_service.domain.model.Traceability;
import com.foodcourt.plaza_service.domain.spi.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @Mock private IMessagingPersistencePort messagingPersistencePort;
    @Mock private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    @Test
    void testCreateOrder_Success() {
        // Arrange
        Long customerId = 10L;
        String customerEmail = "cliente@example.com"; // Email de prueba
        Order orderToSave = new Order(null, null, null, null, null, 5L, null);
        Order savedOrder = new Order(1L, customerId, null, "PENDIENTE", null, 5L, null);
        List<OrderDish> dishes = Collections.singletonList(new OrderDish(null, 1L, 2));

        // Simulamos el comportamiento de los puertos
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(customerId);
        when(userContextProviderPort.getAuthenticatedUserEmail()).thenReturn(customerEmail); // <-- 1. AÑADIMOS ESTA SIMULACIÓN
        when(orderPersistencePort.existsByCustomerIdAndStatusIn(eq(customerId), anyList())).thenReturn(false);
        when(orderPersistencePort.saveOrder(any(Order.class))).thenReturn(savedOrder);

        // Act
        orderUseCase.createOrder(orderToSave, dishes);

        // Assert
        verify(orderPersistencePort).saveOrder(any(Order.class));
        verify(orderPersistencePort).saveOrderDish(anyList());

        ArgumentCaptor<Traceability> traceCaptor = ArgumentCaptor.forClass(Traceability.class);
        verify(traceabilityPersistencePort).logOrderTrace(traceCaptor.capture());
        Traceability capturedTrace = traceCaptor.getValue();

        // Verificamos los datos capturados en el objeto de trazabilidad
        assertEquals(savedOrder.getId(), capturedTrace.getOrderId());
        assertEquals(customerId, capturedTrace.getCustomerId());
        assertEquals("PENDIENTE", capturedTrace.getNewStatus());
        assertNull(capturedTrace.getPreviousStatus());
        assertEquals(customerEmail, capturedTrace.getCustomerEmail()); // <-- 2. AÑADIMOS ESTA VERIFICACIÓN
    }

    @Test
    void testCreateOrder_FailsWhenClientHasAnActiveOrder() {
        // Arrange
        Long customerId = 10L;
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(customerId);
        when(orderPersistencePort.existsByCustomerIdAndStatusIn(eq(customerId), anyList())).thenReturn(true);

        // Act & Assert
        assertThrows(ClientHasAnOrderException.class, () -> {
            orderUseCase.createOrder(new Order(), Collections.emptyList());
        });

        // Verificamos que no se intentó guardar nada
        verify(orderPersistencePort, never()).saveOrder(any());
        verify(traceabilityPersistencePort, never()).logOrderTrace(any());
    }

    @Test
    void testListOrdersByStatus_Success() {
        String status = "PENDIENTE";
        int page = 0;
        int size = 10;
        Long employeeId = 66L;
        Long restaurantId = 31L;
        Page<Order> expectedPage = new PageImpl<>(Collections.emptyList());

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findByRestaurantIdAndStatus(eq(restaurantId), eq(status), any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<Order> result = orderUseCase.listOrdersByStatus(status, page, size);

        verify(employeePersistencePort, times(1)).findRestaurantIdByEmployeeId(employeeId);

        verify(orderPersistencePort, times(1)).findByRestaurantIdAndStatus(eq(restaurantId), eq(status), any(Pageable.class));

        assertEquals(expectedPage, result);
    }

    @Test
    void testAssignOrderToEmployee_Success() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 66L;
        Long restaurantId = 31L;
        Order pendingOrder = new Order(orderId, 10L, LocalDate.now(), "PENDIENTE", null, restaurantId, null);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(restaurantId);

        // Act
        orderUseCase.assignOrderToEmployee(orderId);

        // Assert
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();

        assertEquals("EN_PREPARACION", capturedOrder.getStatus());
        assertEquals(employeeId, capturedOrder.getChefId());

        verify(traceabilityPersistencePort, times(1)).logOrderTrace(any(Traceability.class));
    }

    @Test
    void testAssignOrderToEmployee_FailsWhenOrderNotFound() {
        // Arrange
        Long orderId = 1L;
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> orderUseCase.assignOrderToEmployee(orderId));
        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    void testAssignOrderToEmployee_FailsWhenOrderIsNotPending() {
        // Arrange
        Long orderId = 1L;
        Order inPreparationOrder = new Order(orderId, 10L, LocalDate.now(), "EN_PREPARACION", 55L, 31L, null);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(inPreparationOrder));

        // Act & Assert
        assertThrows(OrderCannotBeAssignedException.class, () -> orderUseCase.assignOrderToEmployee(orderId));
        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    void testAssignOrderToEmployee_FailsWhenEmployeeDoesNotBelongToRestaurant() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 66L;
        Long orderRestaurantId = 31L;
        Long employeeRestaurantId = 99L; // <-- Restaurante incorrecto
        Order pendingOrder = new Order(orderId, 10L, LocalDate.now(), "PENDIENTE", null, orderRestaurantId, null);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(employeeRestaurantId);

        // Act & Assert
        assertThrows(NotRestaurantOwnerException.class, () -> orderUseCase.assignOrderToEmployee(orderId));
        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    void testNotifyOrderReady_Success() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 66L;
        Long customerId = 5L;
        Long restaurantId = 31L;
        String customerPhone = "+573001234567";
        Order orderInPreparation = new Order(orderId, customerId, LocalDate.now(), "EN_PREPARACION", employeeId, restaurantId, null);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(orderInPreparation));
        when(employeePersistencePort.findRestaurantIdByEmployeeId(employeeId)).thenReturn(restaurantId);
        when(userPersistencePort.findUserPhoneById(customerId)).thenReturn(customerPhone);

        // Act
        orderUseCase.notifyOrderReady(orderId);

        // Assert
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();

        assertEquals("LISTO", capturedOrder.getStatus());
        assertNotNull(capturedOrder.getSecurityPin());
        assertEquals(4, capturedOrder.getSecurityPin().length());

        verify(messagingPersistencePort, times(1)).sendNotification(eq(customerPhone), anyString());
        verify(traceabilityPersistencePort, times(1)).logOrderTrace(any(Traceability.class));
    }

    @Test
    void testNotifyOrderReady_FailsWhenOrderIsNotInPreparation() {
        // Arrange
        Long orderId = 1L;
        Order pendingOrder = new Order(orderId, 5L, LocalDate.now(), "PENDIENTE", null, 31L, null);

        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(66L);
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(pendingOrder));

        // Act & Assert
        assertThrows(OrderIsNotInPreparationException.class, () -> orderUseCase.notifyOrderReady(orderId));
        verify(messagingPersistencePort, never()).sendNotification(anyString(), anyString());
    }
}
