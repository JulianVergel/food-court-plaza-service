package com.foodcourt.plaza_service.infrastructure.output.jpa.adapter;

import com.foodcourt.plaza_service.domain.model.PaginationResponse;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.RestaurantEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.PaginationMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.IRestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantJpaRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurant);
        restaurantRepository.save(restaurantEntity);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id).map(restaurantEntityMapper::toRestaurant);
    }

    @Override
    public PaginationResponse<Restaurant> listAllRestaurants(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPageNumber(),
                paginationRequest.getPageSize(),
                Sort.by("name").ascending()
        );

        Page<RestaurantEntity> springPage = restaurantRepository.findAll(pageable);

        return PaginationMapper.toDomainPage(springPage, restaurantEntityMapper::toRestaurant);
    }
}
