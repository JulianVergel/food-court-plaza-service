package com.foodcourt.plaza_service.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishListResponseDto {
    private String name;
    private String description;
    private Long price;
    private String imageUrl;
}
