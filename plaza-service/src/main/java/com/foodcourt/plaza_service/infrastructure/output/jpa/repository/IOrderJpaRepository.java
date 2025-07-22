package com.foodcourt.plaza_service.infrastructure.output.jpa.repository;

import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IOrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByCustomerIdAndStatusIn(Long customerId, List<String> statuses);
}
