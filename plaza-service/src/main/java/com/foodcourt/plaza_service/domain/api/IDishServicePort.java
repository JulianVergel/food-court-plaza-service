package com.foodcourt.plaza_service.domain.api;

import com.foodcourt.plaza_service.domain.model.Dish;
import org.springframework.data.domain.Page;

public interface IDishServicePort {
    void saveDish(Dish dish);
    void updateDish(Long id, Dish dish);
    void enableDisableDish(Long id, boolean enable);
    Page<Dish> listDishes(Long restaurantId, Long categoryId, int page, int size);
}
