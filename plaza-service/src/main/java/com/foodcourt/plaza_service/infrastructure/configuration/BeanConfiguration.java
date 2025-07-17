package com.foodcourt.plaza_service.infrastructure.configuration;

import com.foodcourt.plaza_service.domain.api.IDishServicePort;
import com.foodcourt.plaza_service.domain.spi.ICategoryPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IDishPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import com.foodcourt.plaza_service.domain.usecase.DishUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    // ... (otros beans que puedas tener, como el de RestaurantUseCase)

    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort,
                                            IRestaurantPersistencePort restaurantPersistencePort,
                                            ICategoryPersistencePort categoryPersistencePort,
                                            IUserContextProviderPort userContextProviderPort) {
        return new DishUseCase(dishPersistencePort, restaurantPersistencePort, categoryPersistencePort, userContextProviderPort);
    }
}
