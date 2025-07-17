package com.foodcourt.plaza_service.domain.exception;

import org.junit.jupiter.api.Test;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.USER_NOT_AN_OWNER_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserNotAnOwnerExceptionTest {
    @Test
    void testExceptionCreation() {
        // Act
        UserNotAnOwnerException exception = new UserNotAnOwnerException();

        // Assert
        assertEquals(USER_NOT_AN_OWNER_MESSAGE, exception.getMessage());
    }
}
