package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Restaurant;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);
}
