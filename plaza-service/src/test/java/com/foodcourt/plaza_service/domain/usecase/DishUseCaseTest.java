package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.exception.CategoryNotFoundException;
import com.foodcourt.plaza_service.domain.exception.NotRestaurantOwnerException;
import com.foodcourt.plaza_service.domain.exception.RestaurantNotFoundException;
import com.foodcourt.plaza_service.domain.model.Category;
import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.model.Restaurant;
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

    @BeforeEach
    void setUp() {
        // Objeto principal para las pruebas
        dish = new Dish(1L, "Plato de Prueba", "Descripción", 10000L, "url.com/img.png", true, 10L, 20L);
        // Restaurante que simularemos encontrar en la BD
        restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerUserId(5L); // El dueño es el usuario con ID 5
        // Categoría que simularemos encontrar
        category = new Category(20L, "Categoría de Prueba", "Descripción");
    }

    @Test
    void testSaveDish_Success() {
        // Arrange
        // 1. Simulamos que el usuario autenticado es el 5 (el dueño)
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        // 2. Simulamos que el restaurante se encuentra
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));
        // 3. Simulamos que la categoría se encuentra
        when(categoryPersistencePort.findById(20L)).thenReturn(Optional.of(category));

        // Act
        dishUseCase.saveDish(dish);

        // Assert
        // Verificamos que el plato se mandó a guardar
        verify(dishPersistencePort, times(1)).saveDish(dish);
        // Verificamos que el estado 'activo' se estableció en true
        assertTrue(dish.isActive());
    }

    @Test
    void testSaveDish_FailsWhenRestaurantNotFound() {
        // Arrange
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        // Simulamos que el restaurante NO se encuentra
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RestaurantNotFoundException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any()); // No se debe guardar nada
    }

    @Test
    void testSaveDish_FailsWhenUserIsNotOwner() {
        // Arrange
        // Simulamos que el usuario autenticado es el 99, NO el dueño
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(99L);
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testSaveDish_FailsWhenCategoryNotFound() {
        // Arrange
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));
        // Simulamos que la categoría NO se encuentra
        when(categoryPersistencePort.findById(20L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testSaveDish_FailsWithInvalidPrice() {
        // Arrange
        dish.setPrice(0L); // Precio inválido
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));
        when(categoryPersistencePort.findById(20L)).thenReturn(Optional.of(category));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> dishUseCase.saveDish(dish));
        verify(dishPersistencePort, never()).saveDish(any());
    }
}
