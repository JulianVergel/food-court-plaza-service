package com.foodcourt.plaza_service.application.handler;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import com.foodcourt.plaza_service.application.dto.response.RestaurantListResponseDto;
import org.springframework.data.domain.Page;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    Page<RestaurantListResponseDto> listRestaurants(int page, int size);
}
