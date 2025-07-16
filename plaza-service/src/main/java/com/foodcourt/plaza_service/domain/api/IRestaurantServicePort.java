package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;

public interface IRestaurantServicePort {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
}
