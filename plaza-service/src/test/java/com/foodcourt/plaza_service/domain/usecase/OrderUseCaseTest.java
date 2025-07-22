package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.exception.ClientHasAnOrderException;
import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import com.foodcourt.plaza_service.domain.model.Traceability;
import com.foodcourt.plaza_service.domain.spi.IOrderPersistencePort;
import com.foodcourt.plaza_service.domain.spi.ITraceabilityPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

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

    @InjectMocks
    private OrderUseCase orderUseCase;

    @Test
    void testCreateOrder_Success() {
        // Arrange
        Long customerId = 10L;
        String customerEmail = "cliente@example.com"; // Email de prueba
        Order orderToSave = new Order(null, null, null, null, null, 5L);
        Order savedOrder = new Order(1L, customerId, null, "PENDIENTE", null, 5L);
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
}
