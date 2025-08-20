package com.foodcourt.plaza_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String imageUrl;
    private boolean active;
    private Long restaurantId;
    private Long categoryId;
}
