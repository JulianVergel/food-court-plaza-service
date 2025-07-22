package com.foodcourt.plaza_service.application.dto.response;

import java.time.LocalDate;

public class OrderResponseDto {
    private Long id;
    private Long customerId;
    private LocalDate orderDate;
    private String status;
    private Long chefId;
    private Long restaurantId;
}
