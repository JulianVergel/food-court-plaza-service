package com.foodcourt.plaza_service.application.handler.impl;

import com.foodcourt.plaza_service.application.dto.request.OrderRequestDto;
import com.foodcourt.plaza_service.application.handler.IOrderHandler;
import com.foodcourt.plaza_service.application.mapper.request.IOrderRequestMapper;
import com.foodcourt.plaza_service.domain.api.IOrderServicePort;
import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import com.foodcourt.plaza_service.domain.usecase.OrderUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;

    @Override
    public void createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        List<OrderDish> orderDishes = orderRequestMapper.toOrderDishList(orderRequestDto.getDishes());
        logger.info("Restaurant ID recibido en el Handler: {}", order.getRestaurantId());
        order.setRestaurantId(orderRequestDto.getRestaurantId());
        orderServicePort.createOrder(order, orderDishes);
    }
}
