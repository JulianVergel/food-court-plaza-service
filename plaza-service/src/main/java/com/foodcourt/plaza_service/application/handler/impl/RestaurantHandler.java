package com.foodcourt.plaza_service.application.handler.impl;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import com.foodcourt.plaza_service.application.dto.response.RestaurantListResponseDto;
import com.foodcourt.plaza_service.application.handler.IRestaurantHandler;
import com.foodcourt.plaza_service.application.mapper.request.IRestaurantRequestMapper;
import com.foodcourt.plaza_service.application.mapper.response.IRestaurantResponseMapper;
import com.foodcourt.plaza_service.domain.api.IRestaurantServicePort;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;


    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        restaurantServicePort.saveRestaurant(restaurant);
    }

    @Override
    public Page<RestaurantListResponseDto> listRestaurants(int page, int size) {
        Page<Restaurant> restaurantPage = restaurantServicePort.listRestaurants(page, size);
        return restaurantPage.map(restaurantResponseMapper::toListResponse);
    }
}
