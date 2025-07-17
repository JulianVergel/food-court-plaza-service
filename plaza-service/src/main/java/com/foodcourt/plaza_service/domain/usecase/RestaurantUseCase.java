package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import com.foodcourt.plaza_service.application.mapper.request.IRestaurantRequestMapper;
import com.foodcourt.plaza_service.domain.api.IRestaurantServicePort;
import com.foodcourt.plaza_service.domain.exception.UserNotAnOwnerException;
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
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IUserValidationPort userValidationPort;

    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        if (!userValidationPort.isUserOwner(restaurantRequestDto.getOwnerUserId())) {
            throw new UserNotAnOwnerException();
        }

        RestaurantValidator.validateRestaurantRequest(restaurantRequestDto);

        Restaurant restaurant = restaurantRequestMapper.toRestaurant(restaurantRequestDto);

        restaurantPersistencePort.saveRestaurant(restaurant);
    }
}
