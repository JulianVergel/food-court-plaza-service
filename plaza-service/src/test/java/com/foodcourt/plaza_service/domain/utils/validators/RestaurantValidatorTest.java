package com.foodcourt.plaza_service.domain.utils.validators;

import com.foodcourt.plaza_service.domain.exception.InvalidNitException;
import com.foodcourt.plaza_service.domain.exception.InvalidPhoneException;
import com.foodcourt.plaza_service.domain.exception.InvalidRestaurantNameException;
import com.foodcourt.plaza_service.domain.utils.validations.RestaurantValidator;
import org.junit.jupiter.api.Test;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testValidateName_ThrowsExceptionWhenOnlyNumbers() {
        String invalidName = "12345";

        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class,
                () -> RestaurantValidator.validateName(invalidName));

        assertEquals(NAME_FIELD_NOT_ONLY_NUMBERS_MESSAGE, exception.getMessage());
    }

    @Test
    void testValidateNit_ThrowsExceptionWhenNonNumeric() {
        String invalidNit = "123a45";

        InvalidNitException exception = assertThrows(InvalidNitException.class,
                () -> RestaurantValidator.validateNit(invalidNit));

        assertEquals(NIT_FIELD_MUST_BE_NUMERIC_MESSAGE, exception.getMessage());
    }

    @Test
    void testValidatePhone_ThrowsExceptionWithInvalidFormat() {
        String invalidPhone = "abc-123";

        InvalidPhoneException exception = assertThrows(InvalidPhoneException.class,
                () -> RestaurantValidator.validatePhone(invalidPhone));

        assertEquals(PHONE_FIELD_INVALID_MESSAGE, exception.getMessage());
    }

    @Test
    void testValidatePhone_ThrowsExceptionWithInvalidLength() {
        String invalidPhone = "+1234567890123"; // 14 caracteres, excede el límite

        InvalidPhoneException exception = assertThrows(InvalidPhoneException.class,
                () -> RestaurantValidator.validatePhone(invalidPhone));

        assertEquals(PHONE_FIELD_INVALID_MESSAGE, exception.getMessage());
    }

    @Test
    void testValidatePhone_FailsWithInvalidFormatAndLength() {
        // Arrange
        // Este número falla tanto el formato (contiene 'a') como la longitud (14 caracteres)
        String invalidPhone = "+57300123a4567";

        // Act & Assert
        assertThrows(InvalidPhoneException.class, () -> {
            RestaurantValidator.validatePhone(invalidPhone);
        });
    }
}
