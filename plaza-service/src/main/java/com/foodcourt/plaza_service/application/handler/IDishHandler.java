package com.foodcourt.plaza_service.application.handler;

import com.foodcourt.plaza_service.application.dto.request.DishRequestDto;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto);
}
