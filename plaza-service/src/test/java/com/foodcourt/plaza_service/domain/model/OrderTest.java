package com.foodcourt.plaza_service.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void testOrderModel() {
        Order order = new Order(1L, 10L, LocalDate.now(), "PENDIENTE", null, 5L, null);
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals("PENDIENTE", order.getStatus());
    }
}
