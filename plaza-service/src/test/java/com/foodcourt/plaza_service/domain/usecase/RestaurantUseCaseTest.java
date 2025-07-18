package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.exception.UserNotAnOwnerException;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserValidationPort userValidationPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Válido");
        restaurant.setNit("123456789");
        restaurant.setAddress("Calle Falsa 123");
        restaurant.setPhone("+573101234567");
        restaurant.setLogoUrl("http://logo.com/logo.png");
        restaurant.setOwnerUserId(5L); // ID del propietario
    }

    @Test
    void testSaveRestaurant_Success() {
        when(userValidationPort.isUserOwner(5L)).thenReturn(true);

        restaurantUseCase.saveRestaurant(restaurant);

        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurant);
    }

    @Test
    void testSaveRestaurant_FailsWhenUserIsNotAnOwner() {
        when(userValidationPort.isUserOwner(5L)).thenReturn(false);

        assertThrows(UserNotAnOwnerException.class, () -> restaurantUseCase.saveRestaurant(restaurant));

        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    void testSaveRestaurant_FailsWithInvalidName() {
        restaurant.setName("12345"); // Nombre inválido
        when(userValidationPort.isUserOwner(5L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> restaurantUseCase.saveRestaurant(restaurant));

        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    void testListRestaurants() {
        int page = 0;
        int size = 10;
        Page<Restaurant> expectedPage = new PageImpl<>(Collections.singletonList(new Restaurant()));

        when(restaurantPersistencePort.listAllRestaurants(any(Pageable.class))).thenReturn(expectedPage);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        Page<Restaurant> result = restaurantUseCase.listRestaurants(page, size);

        verify(restaurantPersistencePort).listAllRestaurants(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertEquals(page, capturedPageable.getPageNumber());
        assertEquals(size, capturedPageable.getPageSize());
        assertEquals(Sort.by("name").ascending(), capturedPageable.getSort());

        assertEquals(expectedPage, result);
    }
}