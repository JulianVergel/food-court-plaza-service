package com.foodcourt.plaza_service.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDishRequestDto {
    private Long dishId;
    private int quantity;
}
