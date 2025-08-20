package com.foodcourt.plaza_service.application.handler;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import com.foodcourt.plaza_service.application.dto.response.RestaurantListResponseDto;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    PaginationResponse<RestaurantListResponseDto> listRestaurants(int page, int size);
}
