package com.foodcourt.plaza_service.infrastructure.output.jpa.repository;

import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishJpaRepository extends JpaRepository<DishEntity, Long> {
}
