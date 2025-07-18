package com.foodcourt.plaza_service.infrastructure.output.jpa.adapter;

import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.RestaurantEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.IRestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Restaurant> listAllRestaurants(Pageable pageable) {
        Page<RestaurantEntity> restaurantEntityPage = restaurantRepository.findAll(pageable);
        return restaurantEntityPage.map(restaurantEntityMapper::toRestaurant);
    }
}
