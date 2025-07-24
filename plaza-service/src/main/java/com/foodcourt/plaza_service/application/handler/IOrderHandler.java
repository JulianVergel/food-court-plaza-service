package com.foodcourt.plaza_service.application.handler;

import com.foodcourt.plaza_service.application.dto.request.OrderDeliverRequestDto;
import com.foodcourt.plaza_service.application.dto.request.OrderRequestDto;
import com.foodcourt.plaza_service.application.dto.response.OrderResponseDto;
import org.springframework.data.domain.Page;

public interface IOrderHandler {
    void createOrder(OrderRequestDto orderRequestDto);
    Page<OrderResponseDto> listOrdersByStatus(String status, int page, int size);
    void assignOrderToEmployee(Long orderId);
    void notifyOrderReady(Long orderId);
    void deliverOrder(Long orderId, OrderDeliverRequestDto orderDeliverRequestDto);
}
