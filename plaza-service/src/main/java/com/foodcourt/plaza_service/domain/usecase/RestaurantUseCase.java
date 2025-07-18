package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.api.IRestaurantServicePort;
import com.foodcourt.plaza_service.domain.exception.UserNotAnOwnerException;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserValidationPort;
import com.foodcourt.plaza_service.domain.utils.validations.RestaurantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserValidationPort userValidationPort;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        if (!userValidationPort.isUserOwner(restaurant.getOwnerUserId())) {
            throw new UserNotAnOwnerException();
        }

        RestaurantValidator.validateRestaurantRequest(restaurant);
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public Page<Restaurant> listRestaurants(int page, int size) {
        // Creamos un objeto Pageable que ordena por el campo 'name' de forma ascendente
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return restaurantPersistencePort.listAllRestaurants(pageable);
    }
}
