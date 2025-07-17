package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Dish;

public interface IDishPersistencePort {
    void saveDish(Dish dish);
}
