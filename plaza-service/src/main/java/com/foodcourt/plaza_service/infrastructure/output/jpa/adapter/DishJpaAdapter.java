package com.foodcourt.plaza_service.infrastructure.output.jpa.adapter;

import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.spi.IDishPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.DishEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.IDishJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishJpaRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public void saveDish(Dish dish) {
        DishEntity dishEntity = dishEntityMapper.toDishEntity(dish);
        dishRepository.save(dishEntity);
    }
}
