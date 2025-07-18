package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);
    Optional<Restaurant> findById(Long id);
    Page<Restaurant> listAllRestaurants(Pageable pageable);
}
