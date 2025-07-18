package com.foodcourt.plaza_service.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequestDto {
    private String name;
    private Long price;
    private String description;
    private String imageUrl;
    private Long categoryId;
    private Long restaurantId;
}
