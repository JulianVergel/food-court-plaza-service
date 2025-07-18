package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.api.IDishServicePort;
import com.foodcourt.plaza_service.domain.exception.CategoryNotFoundException;
import com.foodcourt.plaza_service.domain.exception.NotRestaurantOwnerException;
import com.foodcourt.plaza_service.domain.exception.RestaurantNotFoundException;
import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.ICategoryPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IDishPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import lombok.RequiredArgsConstructor;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.PRICE_DISH_NOT_VALID_MESSAGE;

@RequiredArgsConstructor
public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IUserContextProviderPort userContextProviderPort;

    @Override
    public void saveDish(Dish dish) {
        Long ownerId = userContextProviderPort.getAuthenticatedUserId();

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId())
                .orElseThrow(RestaurantNotFoundException::new);

        if (!restaurant.getOwnerUserId().equals(ownerId)) {
            throw new NotRestaurantOwnerException();
        }

        categoryPersistencePort.findById(dish.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        if (dish.getPrice() <= 0) {
            throw new IllegalArgumentException(PRICE_DISH_NOT_VALID_MESSAGE);
        }

        dish.setActive(true);
        dishPersistencePort.saveDish(dish);
    }
}
