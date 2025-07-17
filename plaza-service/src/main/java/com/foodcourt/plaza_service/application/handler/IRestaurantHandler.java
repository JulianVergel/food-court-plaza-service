package com.foodcourt.plaza_service.application.handler;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
}
