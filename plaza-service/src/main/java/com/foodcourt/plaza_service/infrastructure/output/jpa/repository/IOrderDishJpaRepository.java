package com.foodcourt.plaza_service.infrastructure.output.jpa.repository;

import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.OrderDishEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.OrderDishPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderDishJpaRepository extends JpaRepository<OrderDishEntity, OrderDishPK> {

}
