package com.foodcourt.plaza_service.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryModel() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Comida Italiana");
        category.setDescription("Platos originarios de Italia.");

        assertEquals(1L, category.getId());
        assertEquals("Comida Italiana", category.getName());
        assertEquals("Platos originarios de Italia.", category.getDescription());
    }
}
