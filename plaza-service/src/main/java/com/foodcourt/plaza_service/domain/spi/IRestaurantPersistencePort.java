package com.foodcourt.plaza_service.domain.spi;

import com.foodcourt.plaza_service.domain.model.PaginationResponse;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;
import com.foodcourt.plaza_service.domain.model.Restaurant;

import java.util.Optional;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);
    Optional<Restaurant> findById(Long id);
    PaginationResponse<Restaurant> listAllRestaurants(PaginationRequest paginationRequest);
}
