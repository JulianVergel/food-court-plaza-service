package com.foodcourt.plaza_service.application.handler.impl;

import com.foodcourt.plaza_service.application.dto.request.OrderRequestDto;
import com.foodcourt.plaza_service.application.dto.response.OrderResponseDto;
import com.foodcourt.plaza_service.application.handler.IOrderHandler;
import com.foodcourt.plaza_service.application.mapper.request.IOrderRequestMapper;
import com.foodcourt.plaza_service.application.mapper.response.IOrderResponseMapper;
import com.foodcourt.plaza_service.domain.api.IOrderServicePort;
import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public void createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        List<OrderDish> orderDishes = orderRequestMapper.toOrderDishList(orderRequestDto.getDishes());
        order.setRestaurantId(orderRequestDto.getRestaurantId());
        orderServicePort.createOrder(order, orderDishes);
    }

    @Override
    public Page<OrderResponseDto> listOrdersByStatus(String status, int page, int size) {
        Page<Order> orderPage = orderServicePort.listOrdersByStatus(status, page, size);
        return orderPage.map(orderResponseMapper::toResponse);
    }
}
