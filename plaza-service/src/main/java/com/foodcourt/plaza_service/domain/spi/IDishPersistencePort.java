package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IDishPersistencePort {
    void saveDish(Dish dish);
    Optional<Dish> findById(Long id);
    Page<Dish> listDishesByRestaurant(Long restaurantId, Long categoryId, Pageable pageable);
}
