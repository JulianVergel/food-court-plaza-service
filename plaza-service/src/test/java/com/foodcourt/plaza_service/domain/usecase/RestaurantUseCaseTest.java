package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import com.foodcourt.plaza_service.application.mapper.request.IRestaurantRequestMapper;
import com.foodcourt.plaza_service.domain.exception.UserNotAnOwnerException;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;
    @Mock
    private IUserValidationPort userValidationPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantRequestDto restaurantRequestDto;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        // Objeto de entrada para los tests
        restaurantRequestDto = new RestaurantRequestDto();
        restaurantRequestDto.setName("Restaurante de Prueba");
        restaurantRequestDto.setNit("123456789");
        restaurantRequestDto.setAddress("Calle Falsa 123");
        restaurantRequestDto.setPhone("+573101234567");
        restaurantRequestDto.setLogoUrl("http://logo.com/logo.png");
        restaurantRequestDto.setOwnerUserId(1L);

        // Objeto de dominio que simula el resultado del mapper
        restaurant = new Restaurant(1L, "Restaurante de Prueba", "Calle Falsa 123", 1L, "+573101234567", "http://logo.com/logo.png", "123456789");
    }

    @Test
    void testSaveRestaurant_Success() {
        // Arrange
        when(userValidationPort.isUserOwner(1L)).thenReturn(true);
        when(restaurantRequestMapper.toRestaurant(restaurantRequestDto)).thenReturn(restaurant);

        // Act
        restaurantUseCase.saveRestaurant(restaurantRequestDto);

        // Assert
        // Verificamos que el método de persistencia fue llamado exactamente una vez con el objeto correcto.
        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurant);
    }

    @Test
    void testSaveRestaurant_FailsWhenUserIsNotAnOwner() {
        // Arrange (AC #2)
        when(userValidationPort.isUserOwner(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(UserNotAnOwnerException.class, () -> restaurantUseCase.saveRestaurant(restaurantRequestDto));

        // Verificamos que, al fallar la validación, nunca se intentó guardar el restaurante.
        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void testSaveRestaurant_FailsWithInvalidNit() {
        // Arrange (AC #3)
        restaurantRequestDto.setNit("123a"); // NIT inválido
        when(userValidationPort.isUserOwner(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> restaurantUseCase.saveRestaurant(restaurantRequestDto));

        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void testSaveRestaurant_FailsWithInvalidPhone() {
        // Arrange (AC #3)
        restaurantRequestDto.setPhone("12345678901234"); // Teléfono inválido (muy largo)
        when(userValidationPort.isUserOwner(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> restaurantUseCase.saveRestaurant(restaurantRequestDto));

        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void testSaveRestaurant_FailsWithInvalidName() {
        // Arrange (AC #4)
        restaurantRequestDto.setName("12345"); // Nombre inválido (solo números)
        when(userValidationPort.isUserOwner(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> restaurantUseCase.saveRestaurant(restaurantRequestDto));

        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }
}