package com.foodcourt.plaza_service.domain.utils.validations;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import org.junit.jupiter.api.Test;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantValidatorTest {

    @Test
    void testValidateName_Valid() {
        // Arrange
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante 123");
        dto.setNit("12345");
        dto.setPhone("+12345");

        // Act & Assert
        assertDoesNotThrow(() -> RestaurantValidator.validateRestaurantRequest(dto));
    }

    @Test
    void testValidateName_InvalidOnlyNumbers() {
        // Arrange
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("12345"); // Nombre inválido
        dto.setNit("12345");
        dto.setPhone("+12345");


        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> RestaurantValidator.validateRestaurantRequest(dto));
        assertEquals(NAME_FIELD_NOT_ONLY_NUMBERS_MESSAGE, exception.getMessage());
    }

    @Test
    void testValidateNit_InvalidNonNumeric() {
        // Arrange
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante");
        dto.setNit("123a45"); // NIT inválido
        dto.setPhone("+12345");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> RestaurantValidator.validateRestaurantRequest(dto));
        assertEquals(NIT_FIELD_MUST_BE_NUMERIC_MESSAGE, exception.getMessage());
    }

    @Test
    void testValidatePhone_InvalidFormat() {
        // Arrange
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante");
        dto.setNit("12345");
        dto.setPhone("abc"); // Teléfono inválido

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> RestaurantValidator.validateRestaurantRequest(dto));
        assertEquals(PHONE_FIELD_INVALID_MESSAGE, exception.getMessage());
    }

    @Test
    void testValidatePhone_InvalidLength() {
        // Arrange
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante");
        dto.setNit("12345");
        dto.setPhone("+1234567890123"); // Teléfono inválido (muy largo, 14 chars)

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> RestaurantValidator.validateRestaurantRequest(dto));
        assertEquals(PHONE_FIELD_INVALID_MESSAGE, exception.getMessage());
    }
}