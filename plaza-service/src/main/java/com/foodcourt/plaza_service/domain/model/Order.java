package com.foodcourt.plaza_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long customerId;
    private LocalDate orderDate;
    private String status;
    private Long chefId;
    private Long restaurantId;
    private String securityPin;
}
