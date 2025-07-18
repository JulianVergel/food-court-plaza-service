package com.foodcourt.plaza_service.application.mapper.request;

import com.foodcourt.plaza_service.application.dto.request.DishRequestDto;
import com.foodcourt.plaza_service.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {
    Dish toDish(DishRequestDto dishRequestDto);
}
