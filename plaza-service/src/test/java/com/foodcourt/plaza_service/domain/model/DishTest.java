package com.foodcourt.plaza_service.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DishTest {

    @Test
    void testDishModel() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza Hawaiana");
        dish.setDescription("Pizza con piña y jamón");
        dish.setPrice(25000L);
        dish.setImageUrl("http://example.com/pizza.png");
        dish.setActive(true);
        dish.setRestaurantId(10L);
        dish.setCategoryId(2L);

        assertEquals(1L, dish.getId());
        assertEquals("Pizza Hawaiana", dish.getName());
        assertEquals("Pizza con piña y jamón", dish.getDescription());
        assertEquals(25000L, dish.getPrice());
        assertEquals("http://example.com/pizza.png", dish.getImageUrl());
        assertTrue(dish.isActive());
        assertEquals(10L, dish.getRestaurantId());
        assertEquals(2L, dish.getCategoryId());
    }

    @Test
    void testAllArgsConstructor() {
        Dish dish = new Dish(1L, "Lasaña", "Lasaña de carne", 30000L, "url.com/lasana.png", false, 11L, 3L);

        assertNotNull(dish);
        assertEquals("Lasaña", dish.getName());
    }
}
