package com.foodcourt.plaza_service.application.mapper.request;

import com.foodcourt.plaza_service.application.dto.request.OrderDishRequestDto;
import com.foodcourt.plaza_service.application.dto.request.OrderRequestDto;
import com.foodcourt.plaza_service.domain.model.Order;
import com.foodcourt.plaza_service.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {

    @Mapping(target = "restaurantId", source = "restaurantId")
    Order toOrder(OrderRequestDto orderRequestDto);

    List<OrderDish> toOrderDishList(List<OrderDishRequestDto> orderDishRequestDtoList);

    @Mapping(target = "dishId", source = "dishId")
    @Mapping(target = "quantity", source = "quantity")
    OrderDish toOrderDish(OrderDishRequestDto orderDishRequestDto);
}
