package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Category;

import java.util.Optional;

public interface ICategoryPersistencePort {
    Optional<Category> findById(Long id);
}
