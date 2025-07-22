package com.foodcourt.plaza_service.infrastructure.output.jpa.adapter;

import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import com.foodcourt.plaza_service.domain.spi.IOrderPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.DishEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.OrderDishEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.OrderDishPK;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.OrderEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.IOrderEntityMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.IOrderDishJpaRepository;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.IOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderJpaRepository orderRepository;
    private final IOrderDishJpaRepository orderDishRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public Order saveOrder(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);
        OrderEntity savedEntity = orderRepository.save(orderEntity);
        return orderEntityMapper.toOrder(savedEntity);
    }

    @Override
    public void saveOrderDish(List<OrderDish> orderDishes) {
        if (orderDishes == null || orderDishes.isEmpty()) {
            return;
        }

        List<OrderDishEntity> orderDishEntities = orderDishes.stream().map(orderDish -> {
            OrderDishPK pk = new OrderDishPK(orderDish.getOrderId(), orderDish.getDishId());

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(orderDish.getOrderId());

            DishEntity dishEntity = new DishEntity();
            dishEntity.setId(orderDish.getDishId());

            return new OrderDishEntity(pk, orderEntity, dishEntity, orderDish.getQuantity());
        }).collect(Collectors.toList());

        orderDishRepository.saveAll(orderDishEntities);
    }

    @Override
    public boolean existsByCustomerIdAndStatusIn(Long customerId, List<String> statuses) {
        return orderRepository.existsByCustomerIdAndStatusIn(customerId, statuses);
    }

    @Override
    public Page<Order> findByRestaurantIdAndStatus(Long restaurantId, String status, Pageable pageable) {
        Page<OrderEntity> orderEntityPage = orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);
        return orderEntityPage.map(orderEntityMapper::toOrder);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderEntityMapper::toOrder);
    }
}
