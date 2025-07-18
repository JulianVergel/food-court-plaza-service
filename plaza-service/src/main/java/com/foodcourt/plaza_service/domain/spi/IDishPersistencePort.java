package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Dish;

import java.util.Optional;

public interface IDishPersistencePort {
    void saveDish(Dish dish);
    Optional<Dish> findById(Long id);
}
