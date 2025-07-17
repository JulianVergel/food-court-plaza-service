package com.foodcourt.plaza_service.application.handler.impl;

import com.foodcourt.plaza_service.application.dto.request.DishRequestDto;
import com.foodcourt.plaza_service.application.handler.IDishHandler;
import com.foodcourt.plaza_service.application.mapper.request.IDishRequestMapper;
import com.foodcourt.plaza_service.domain.api.IDishServicePort;
import com.foodcourt.plaza_service.domain.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;

    @Override
    public void saveDish(DishRequestDto dishRequestDto) {
        Dish dish = dishRequestMapper.toDish(dishRequestDto);
        dishServicePort.saveDish(dish);
    }
}
