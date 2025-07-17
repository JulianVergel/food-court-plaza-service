package com.foodcourt.plaza_service.infrastructure.output.jpa.repository;

import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
}
