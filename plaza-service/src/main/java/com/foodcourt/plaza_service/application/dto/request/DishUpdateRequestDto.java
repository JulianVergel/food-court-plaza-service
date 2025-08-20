package com.foodcourt.plaza_service.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishUpdateRequestDto {
    private Long price;
    private String description;
}
