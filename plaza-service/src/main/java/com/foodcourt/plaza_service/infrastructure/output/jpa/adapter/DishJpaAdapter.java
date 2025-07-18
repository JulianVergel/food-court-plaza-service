package com.foodcourt.plaza_service.infrastructure.output.jpa.adapter;

import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.spi.IDishPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.DishEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.IDishJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id).map(dishEntityMapper::toDish);
    }

    @Override
    public Page<Dish> listDishesByRestaurant(Long restaurantId, Long categoryId, Pageable pageable) {
        Page<DishEntity> dishEntityPage;
        if (categoryId != null) {
            dishEntityPage = dishRepository.findByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable);
        } else {
            dishEntityPage = dishRepository.findByRestaurantId(restaurantId, pageable);
        }
        return dishEntityPage.map(dishEntityMapper::toDish);
    }
}
