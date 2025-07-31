package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.domain.model.Page;
import com.foodcourt.plaza_service.domain.model.Restaurant;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    Page<Restaurant> listRestaurants(int page, int size);
}
