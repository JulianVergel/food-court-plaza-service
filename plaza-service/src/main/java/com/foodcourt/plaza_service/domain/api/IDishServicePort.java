package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.domain.model.Dish;

public interface IDishServicePort {
    void saveDish(Dish dish);
}
