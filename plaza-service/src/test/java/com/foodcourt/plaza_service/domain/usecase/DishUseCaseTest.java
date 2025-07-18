package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.exception.CategoryNotFoundException;
import com.foodcourt.plaza_service.domain.exception.DishNotFoundException;
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
import org.mockito.ArgumentCaptor;

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
        // Objeto principal para las pruebas
        dish = new Dish(1L, "Plato de Prueba", "Descripción", 10000L, "url.com/img.png", true, 10L, 20L);
        restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerUserId(5L);
        category = new Category(20L, "Categoría de Prueba", "Descripción");

        // Nuevo objeto con los datos que simulan la entrada para actualizar
        dishUpdate = new Dish();
        dishUpdate.setPrice(15000L);
        dishUpdate.setDescription("Nueva Descripción");
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

    @Test
    void testUpdateDish_Success() {
        // Arrange
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L); // Usuario es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        // Act
        dishUseCase.updateDish(1L, dishUpdate);

        // Assert
        // Usamos ArgumentCaptor para "capturar" el objeto que se pasa al método saveDish
        ArgumentCaptor<Dish> dishCaptor = ArgumentCaptor.forClass(Dish.class);
        verify(dishPersistencePort).saveDish(dishCaptor.capture());

        Dish capturedDish = dishCaptor.getValue();

        // Verificamos que solo los campos permitidos fueron actualizados
        assertEquals(15000L, capturedDish.getPrice());
        assertEquals("Nueva Descripción", capturedDish.getDescription());
        // Verificamos que los otros campos NO cambiaron
        assertEquals("Plato de Prueba", capturedDish.getName());
        assertEquals(1L, capturedDish.getId());
    }

    @Test
    void testUpdateDish_FailsWhenDishNotFound() {
        // Arrange
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DishNotFoundException.class, () -> dishUseCase.updateDish(1L, dishUpdate));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testUpdateDish_FailsWhenUserIsNotOwner() {
        // Arrange
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(99L); // Usuario NO es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.updateDish(1L, dishUpdate));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testUpdateDish_FailsWithInvalidPrice() {
        // Arrange
        dishUpdate.setPrice(0L); // Precio inválido
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L);
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> dishUseCase.updateDish(1L, dishUpdate));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testEnableDisableDish_EnableSuccess() {
        // Arrange
        dish.setActive(false); // Empezamos con el plato desactivado
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L); // El usuario es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        // Act
        dishUseCase.enableDisableDish(1L, true); // Queremos activarlo

        // Assert
        ArgumentCaptor<Dish> dishCaptor = ArgumentCaptor.forClass(Dish.class);
        verify(dishPersistencePort).saveDish(dishCaptor.capture());
        assertTrue(dishCaptor.getValue().isActive()); // Verificamos que se guardó como 'true'
    }

    @Test
    void testEnableDisableDish_DisableSuccess() {
        // Arrange
        dish.setActive(true); // Empezamos con el plato activado
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(5L); // El usuario es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        // Act
        dishUseCase.enableDisableDish(1L, false); // Queremos desactivarlo

        // Assert
        ArgumentCaptor<Dish> dishCaptor = ArgumentCaptor.forClass(Dish.class);
        verify(dishPersistencePort).saveDish(dishCaptor.capture());
        assertFalse(dishCaptor.getValue().isActive()); // Verificamos que se guardó como 'false'
    }

    @Test
    void testEnableDisableDish_FailsWhenUserIsNotOwner() {
        // Arrange
        when(userContextProviderPort.getAuthenticatedUserId()).thenReturn(99L); // El usuario NO es el dueño
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));
        when(restaurantPersistencePort.findById(10L)).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(NotRestaurantOwnerException.class, () -> dishUseCase.enableDisableDish(1L, true));
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testEnableDisableDish_FailsWhenDishNotFound() {
        // Arrange
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DishNotFoundException.class, () -> dishUseCase.enableDisableDish(1L, true));
        verify(dishPersistencePort, never()).saveDish(any());
    }
}
