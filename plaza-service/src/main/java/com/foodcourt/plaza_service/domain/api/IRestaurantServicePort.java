package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.domain.model.Restaurant;
import org.springframework.data.domain.Page;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    Page<Restaurant> listRestaurants(int page, int size);
}
