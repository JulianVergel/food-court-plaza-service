package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.exception.InvalidRestaurantNameException;
import com.foodcourt.plaza_service.domain.exception.UserNotAnOwnerException;
import com.foodcourt.plaza_service.domain.model.Page;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;
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
        restaurant.setOwnerUserId(5L);
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
        Restaurant restaurant = new Restaurant();
        restaurant.setName("12345");
        restaurant.setNit("123456789");
        restaurant.setPhone("+573001234567");
        restaurant.setOwnerUserId(5L);

        when(userValidationPort.isUserOwner(5L)).thenReturn(true);

        assertThrows(InvalidRestaurantNameException.class, () -> restaurantUseCase.saveRestaurant(restaurant));

        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void testListRestaurants() {
        int page = 0;
        int size = 10;
        Page<Restaurant> expectedPage = new Page<>(Collections.singletonList(new Restaurant()), 1L, 1, page, size);

        when(restaurantPersistencePort.listAllRestaurants(any(PaginationRequest.class))).thenReturn(expectedPage);

        Page<Restaurant> result = restaurantUseCase.listRestaurants(page, size);

        ArgumentCaptor<PaginationRequest> paginationRequestCaptor = ArgumentCaptor.forClass(PaginationRequest.class);
        verify(restaurantPersistencePort).listAllRestaurants(paginationRequestCaptor.capture());
        PaginationRequest capturedRequest = paginationRequestCaptor.getValue();

        assertEquals(page, capturedRequest.getPageNumber());
        assertEquals(size, capturedRequest.getPageSize());

        assertEquals(expectedPage, result);
    }
}