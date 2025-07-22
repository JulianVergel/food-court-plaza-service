package com.foodcourt.plaza_service.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long customerId;
    private LocalDate orderDate;
    private String status;
    private Long chefId;
    private Long restaurantId;
}
