package com.foodcourt.plaza_service.infrastructure.output.jpa.adapter;

import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.model.Page;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;
import com.foodcourt.plaza_service.domain.spi.IDishPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.DishEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.IDishJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<Dish> listDishesByRestaurant(Long restaurantId, Long categoryId, PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPageNumber(), paginationRequest.getPageSize());

        org.springframework.data.domain.Page<DishEntity> springPage;
        if (categoryId != null) {
            springPage = dishRepository.findByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable);
        } else {
            springPage = dishRepository.findByRestaurantId(restaurantId, pageable);
        }

        List<Dish> domainDishes = springPage.getContent()
                .stream()
                .map(dishEntityMapper::toDish)
                .collect(Collectors.toList());

        return new Page<>(
                domainDishes,
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.getNumber(),
                springPage.getSize()
        );
    }
}
