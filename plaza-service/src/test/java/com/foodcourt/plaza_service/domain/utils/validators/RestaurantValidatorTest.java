package com.foodcourt.plaza_service.domain.utils.validators;

import com.foodcourt.plaza_service.domain.utils.validations.RestaurantValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RestaurantValidatorTest {
    @Test
    void testValidateNit_Success() {
        String validNit = "123456789";

        assertDoesNotThrow(() -> RestaurantValidator.validateNit(validNit));
    }

    @Test
    void testValidatePhone_Success() {
        String validPhone = "+573001234567";

        assertDoesNotThrow(() -> RestaurantValidator.validatePhone(validPhone));
    }
}
