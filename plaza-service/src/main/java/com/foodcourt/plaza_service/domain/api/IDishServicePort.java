package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;

public interface IDishServicePort {
    void saveDish(Dish dish);
    void updateDish(Long id, Dish dish);
    void enableDisableDish(Long id, boolean enable);
    PaginationResponse<Dish> listDishes(Long restaurantId, Long categoryId, int page, int size);
}
