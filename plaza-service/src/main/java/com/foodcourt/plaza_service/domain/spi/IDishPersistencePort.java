package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;

import java.util.Optional;

public interface IDishPersistencePort {
    void saveDish(Dish dish);
    Optional<Dish> findById(Long id);
    PaginationResponse<Dish> listDishesByRestaurant(Long restaurantId, Long categoryId, PaginationRequest paginationRequest);
}
