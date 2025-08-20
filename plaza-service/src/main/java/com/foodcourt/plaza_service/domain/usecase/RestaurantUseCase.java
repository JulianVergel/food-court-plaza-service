package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.api.IRestaurantServicePort;
import com.foodcourt.plaza_service.domain.exception.UserNotAnOwnerException;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserValidationPort;
import com.foodcourt.plaza_service.domain.utils.validations.RestaurantValidator;
import lombok.RequiredArgsConstructor;
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
    public PaginationResponse<Restaurant> listRestaurants(int page, int size) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size);
        return restaurantPersistencePort.listAllRestaurants(paginationRequest);
    }
}
