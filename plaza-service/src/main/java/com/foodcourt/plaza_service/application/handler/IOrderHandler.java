package com.foodcourt.plaza_service.application.handler;

import com.foodcourt.plaza_service.application.dto.request.OrderRequestDto;

public interface IOrderHandler {
    void createOrder(OrderRequestDto orderRequestDto);
}
