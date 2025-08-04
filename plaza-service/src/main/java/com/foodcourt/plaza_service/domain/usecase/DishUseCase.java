package com.foodcourt.plaza_service.domain.usecase;

import com.foodcourt.plaza_service.domain.api.IDishServicePort;
import com.foodcourt.plaza_service.domain.exception.CategoryNotFoundException;
import com.foodcourt.plaza_service.domain.exception.NotRestaurantOwnerException;
import com.foodcourt.plaza_service.domain.exception.RestaurantNotFoundException;
import com.foodcourt.plaza_service.domain.exception.DishNotFoundException;
import com.foodcourt.plaza_service.domain.model.Dish;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.ICategoryPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IDishPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import com.foodcourt.plaza_service.domain.utils.constants.DomainConstants;
import lombok.RequiredArgsConstructor;

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

        if (dish.getPrice() <= DomainConstants.MINIMUM_PRICE) {
            throw new IllegalArgumentException(DomainConstants.PRICE_DISH_NOT_VALID_MESSAGE);
        }

        dish.setActive(true);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void updateDish(Long id, Dish dishUpdate) {
        Dish existingDish = findDishAndValidateOwnership(id);

        if (dishUpdate.getPrice() != null && dishUpdate.getPrice() <= DomainConstants.MINIMUM_PRICE) {
            throw new IllegalArgumentException(DomainConstants.PRICE_DISH_NOT_VALID_MESSAGE);
        }

        existingDish.setPrice(dishUpdate.getPrice());
        existingDish.setDescription(dishUpdate.getDescription());

        dishPersistencePort.saveDish(existingDish);
    }

    @Override
    public void enableDisableDish(Long id, boolean enable) {
        Dish existingDish = findDishAndValidateOwnership(id);
        existingDish.setActive(enable);
        dishPersistencePort.saveDish(existingDish);
    }

    @Override
    public PaginationResponse<Dish> listDishes(Long restaurantId, Long categoryId, int page, int size) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size);
        return dishPersistencePort.listDishesByRestaurant(restaurantId, categoryId, paginationRequest);
    }

    private Dish findDishAndValidateOwnership(Long dishId) {
        Long ownerId = userContextProviderPort.getAuthenticatedUserId();

        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(DishNotFoundException::new);

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId())
                .orElseThrow(RestaurantNotFoundException::new);

        if (!restaurant.getOwnerUserId().equals(ownerId)) {
            throw new NotRestaurantOwnerException();
        }
        return dish;
    }
}
