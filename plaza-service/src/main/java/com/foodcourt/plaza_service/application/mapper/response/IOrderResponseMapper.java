package com.foodcourt.plaza_service.application.mapper.response;

import com.foodcourt.plaza_service.application.dto.response.OrderResponseDto;
import com.foodcourt.plaza_service.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {
    OrderResponseDto toResponse(Order order);
}
