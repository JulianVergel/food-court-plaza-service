package com.foodcourt.plaza_service.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderDishTest {
    @Test
    void testOrderDishModel() {
        OrderDish orderDish = new OrderDish(1L, 2L, 3);
        assertNotNull(orderDish);
        assertEquals(1L, orderDish.getOrderId());
        assertEquals(3, orderDish.getQuantity());
    }
}
