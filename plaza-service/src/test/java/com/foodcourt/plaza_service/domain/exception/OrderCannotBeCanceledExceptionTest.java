package com.foodcourt.plaza_service.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderCannotBeCanceledExceptionTest {
    @Test
    void testExceptionIsCreated() {
        assertDoesNotThrow(OrderCannotBeCanceledException::new);
    }
}