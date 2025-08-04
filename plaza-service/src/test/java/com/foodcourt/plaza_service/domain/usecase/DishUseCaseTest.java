package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.exception.CategoryNotFoundException;
import com.foodcourt.plaza_service.domain.exception.DishNotFoundException;
import com.foodcourt.plaza_service.domain.exception.NotRestaurantOwnerException;
import com.foodcourt.plaza_service.domain.exception.RestaurantNotFoundException;
import com.foodcourt.plaza_service.domain.model.*;
import com.foodcourt.plaza_service.domain.spi.ICategoryPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IDishPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private ICategoryPersistencePort categoryPersistencePort;
    @Mock
    private IUserContextProviderPort userContextProviderPort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish dish;
    private Restaurant restaurant;
    private Category category;
    private Dish dishUpdate;

    @BeforeEach
    void setUp() {
        dish = new Dish(1L, "Plato de Prueba", "Descripción", 10000L, "url.com/img.png", true, 10L, 20L);
        restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerUserId(5L);
        category = new Category(20L, "Categoría de Prueba", "Descripción");

        dishUpdate = new Dish();
        dishUpdate.setPrice(15000L);
        dishUpdate.setDescription("Nueva Descripción");
    }

    @Test
    void testSaveDish_Success() {
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));
        when(categoryPersistencePort.findById(20L)).thenReturn(Optional.of(category));

        dishUseCase.saveDish(dish);

        verify(dishPersistencePort, times(1)).saveDish(dish);
        assertTrue(dish.isActive());
    }

    @Test
    void testSaveDish_FailsWhenRestaurantNotFound() {
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any()); // No se debe guardar nada
    }

    @Test
    void testSaveDish_FailsWhenUserIsNotOwner() {
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(99L);
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testSaveDish_FailsWhenCategoryNotFound() {
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));
        when(categoryPersistencePort.findById(20L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testSaveDish_FailsWithInvalidPrice() {
        dish.setPrice(0L); // Precio inválido
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));
        when(categoryPersistencePort.findById(20L)).thenReturn(Optional.of(category));

        assertThrows(IllegalArgumentException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testUpdateDish_Success() {
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L); // Usuario es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        dishUseCase.updateDish(1L, dishUpdate);

        ArgumentCaptor<Dish> dishCaptor = ArgumentCaptor.forClass(Dish.class);
        verify(dishPersistencePort).saveDish(dishCaptor.capture());

        Dish capturedDish = dishCaptor.getValue();

        assertEquals(15000L, capturedDish.getPrice());
        assertEquals("Nueva Descripción", capturedDish.getDescription());
        assertEquals("Plato de Prueba", capturedDish.getName());
        assertEquals(1L, capturedDish.getId());
    }

    @Test
    void testUpdateDish_FailsWhenDishNotFound() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class, () -> dishUseCase.updateDish(1L, dishUpdate));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testUpdateDish_FailsWhenUserIsNotOwner() {
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(99L); // Usuario NO es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.updateDish(1L, dishUpdate));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testUpdateDish_FailsWithInvalidPrice() {
        dishUpdate.setPrice(0L); // Precio inválido
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        assertThrows(IllegalArgumentException.class, () -> dishUseCase.updateDish(1L, dishUpdate));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testEnableDisableDish_EnableSuccess() {
        dish.setActive(false); // Empezamos con el plato desactivado
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L); // El usuario es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        dishUseCase.enableDisableDish(1L, true); // Queremos activarlo

        ArgumentCaptor<Dish> dishCaptor = ArgumentCaptor.forClass(Dish.class);
        verify(dishPersistencePort).saveDish(dishCaptor.capture());
        assertTrue(dishCaptor.getValue().isActive()); // Verificamos que se guardó como 'true'
    }

    @Test
    void testEnableDisableDish_DisableSuccess() {
        dish.setActive(true); // Empezamos con el plato activado
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L); // El usuario es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        dishUseCase.enableDisableDish(1L, false); // Queremos desactivarlo

        ArgumentCaptor<Dish> dishCaptor = ArgumentCaptor.forClass(Dish.class);
        verify(dishPersistencePort).saveDish(dishCaptor.capture());
        assertFalse(dishCaptor.getValue().isActive()); // Verificamos que se guardó como 'false'
    }

    @Test
    void testEnableDisableDish_FailsWhenUserIsNotOwner() {
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(99L); // El usuario NO es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.enableDisableDish(1L, true));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testEnableDisableDish_FailsWhenDishNotFound() {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class, () -> dishUseCase.enableDisableDish(1L, true));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testListDishes_WithCategoryFilter() {
        Long restaurantId = 1L;
        Long categoryId = 5L;
        int page = 0;
        int size = 10;
        PaginationResponse<Dish> expectedPaginationResponse = new PaginationResponse<>(Collections.singletonList(new Dish()), 1L, 1, page, size);

        when(dishPersistencePort.listDishesByRestaurant(eq(restaurantId), eq(categoryId), any(PaginationRequest.class)))
                .thenReturn(expectedPaginationResponse);

        PaginationResponse<Dish> result = dishUseCase.listDishes(restaurantId, categoryId, page, size);

        verify(dishPersistencePort).listDishesByRestaurant(eq(restaurantId), eq(categoryId), any(PaginationRequest.class));
        assertEquals(expectedPaginationResponse, result);
    }

    @Test
    void testListDishes_WithoutCategoryFilter() {
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;
        PaginationResponse<Dish> expectedPaginationResponse = new PaginationResponse<>(Collections.singletonList(new Dish()), 1L, 1, page, size);

        when(dishPersistencePort.listDishesByRestaurant(eq(restaurantId), isNull(), any(PaginationRequest.class)))
                .thenReturn(expectedPaginationResponse);

        PaginationResponse<Dish> result = dishUseCase.listDishes(restaurantId, null, page, size);

        verify(dishPersistencePort).listDishesByRestaurant(eq(restaurantId), isNull(), any(PaginationRequest.class));
        assertEquals(expectedPaginationResponse, result);
    }
}
