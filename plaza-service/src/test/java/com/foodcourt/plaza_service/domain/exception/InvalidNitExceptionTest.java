package com.foodcourt.plaza_service.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidNitExceptionTest {
    @Test
    void testExceptionIsCreated() {
        assertDoesNotThrow(InvalidNitException::new);
    }
}