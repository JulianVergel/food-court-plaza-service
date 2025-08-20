package com.foodcourt.plaza_service.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ClientHasAnOrderExceptionTest {
    @Test
    void testExceptionIsCreated() {
        assertDoesNotThrow(ClientHasAnOrderException::new);
    }
}
