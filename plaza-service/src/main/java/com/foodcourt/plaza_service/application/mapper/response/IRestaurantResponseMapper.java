package com.foodcourt.plaza_service.application.mapper.response;

import com.foodcourt.plaza_service.application.dto.response.RestaurantListResponseDto;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {
    RestaurantListResponseDto toListResponse(Restaurant restaurant);
}
