package com.foodcourt.plaza_service.application.mapper.response;

import com.foodcourt.plaza_service.application.dto.response.DishListResponseDto;
import com.foodcourt.plaza_service.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {
    DishListResponseDto toListResponse(Dish dish);
}
