package com.foodcourt.plaza_service.application.handler.impl;

import com.foodcourt.plaza_service.application.dto.request.DishEnableDisableRequestDto;
import com.foodcourt.plaza_service.application.dto.request.DishRequestDto;
import com.foodcourt.plaza_service.application.dto.request.DishUpdateRequestDto;
import com.foodcourt.plaza_service.application.dto.response.DishListResponseDto;
import com.foodcourt.plaza_service.application.handler.IDishHandler;
import com.foodcourt.plaza_service.application.mapper.request.IDishRequestMapper;
import com.foodcourt.plaza_service.application.mapper.response.IDishResponseMapper;
import com.foodcourt.plaza_service.domain.api.IDishServicePort;
import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void saveDish(DishRequestDto dishRequestDto) {
        Dish dish = dishRequestMapper.toDish(dishRequestDto);
        dishServicePort.saveDish(dish);
    }

    @Override
    public void updateDish(Long id, DishUpdateRequestDto dishUpdateRequestDto) {
        Dish dish = dishRequestMapper.toDish(dishUpdateRequestDto);
        dishServicePort.updateDish(id, dish);
    }

    @Override
    public void enableDisableDish(Long id, DishEnableDisableRequestDto dishEnableDisableRequestDto) {
        dishServicePort.enableDisableDish(id, dishEnableDisableRequestDto.isActive());
    }

    @Override
    public PaginationResponse<DishListResponseDto> listDishes(Long restaurantId, Long categoryId, int page, int size) {
        PaginationResponse<Dish> dishPaginationResponse = dishServicePort.listDishes(restaurantId, categoryId, page, size);
        return dishPaginationResponse.map(dishResponseMapper::toListResponse);
    }
}
