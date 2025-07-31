package com.foodcourt.plaza_service.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantTest {

    @Test
    void testRestaurantModel() {
        Long id = 1L;
        String name = "El Sabor";
        String address = "Calle 10";
        Long ownerId = 2L;
        String phone = "+573001234567";
        String logoUrl = "url.com/logo.png";
        String nit = "12345";

        Restaurant restaurant = new Restaurant(id, name, address, ownerId, phone, logoUrl, nit);

        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(address, restaurant.getAddress());
        assertEquals(ownerId, restaurant.getOwnerUserId());
        assertEquals(phone, restaurant.getPhone());
        assertEquals(logoUrl, restaurant.getLogoUrl());
        assertEquals(nit, restaurant.getNit());
    }
}
