package com.foodcourt.plaza_service.infrastructure.configuration;

import com.foodcourt.plaza_service.domain.api.IDishServicePort;
import com.foodcourt.plaza_service.domain.api.IOrderServicePort;
import com.foodcourt.plaza_service.domain.spi.*;
import com.foodcourt.plaza_service.domain.usecase.DishUseCase;
import com.foodcourt.plaza_service.domain.usecase.OrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort,
                                            IRestaurantPersistencePort restaurantPersistencePort,
                                            ICategoryPersistencePort categoryPersistencePort,
                                            IUserContextProviderPort userContextProviderPort) {
        return new DishUseCase(dishPersistencePort, restaurantPersistencePort, categoryPersistencePort, userContextProviderPort);
    }

    @Bean
    public IOrderServicePort orderServicePort(IOrderPersistencePort orderPersistencePort,
                                              IUserContextProviderPort userContextProviderPort,
                                              ITraceabilityPersistencePort traceabilityPersistencePort,
                                              IEmployeePersistencePort employeePersistencePort) {
        return new OrderUseCase(orderPersistencePort, userContextProviderPort, traceabilityPersistencePort, employeePersistencePort);
    }
}
