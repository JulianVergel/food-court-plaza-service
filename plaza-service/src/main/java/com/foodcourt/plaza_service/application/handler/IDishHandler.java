package com.foodcourt.plaza_service.application.handler;

import com.foodcourt.plaza_service.application.dto.request.DishEnableDisableRequestDto;
import com.foodcourt.plaza_service.application.dto.request.DishRequestDto;
import com.foodcourt.plaza_service.application.dto.request.DishUpdateRequestDto;
import com.foodcourt.plaza_service.application.dto.response.DishListResponseDto;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto);
    void updateDish(Long id, DishUpdateRequestDto dishUpdateRequestDto);
    void enableDisableDish(Long id, DishEnableDisableRequestDto dishEnableDisableRequestDto);
    PaginationResponse<DishListResponseDto> listDishes(Long restaurantId, Long categoryId, int page, int size);
}
