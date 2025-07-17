package com.foodcourt.plaza_service.infrastructure.output.jpa.mapper;

import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishEntityMapper {
    DishEntity toDishEntity(Dish dish);
    Dish toDish(DishEntity dishEntity);
}
