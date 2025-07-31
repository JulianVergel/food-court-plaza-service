package com.foodcourt.plaza_service.infrastructure.output.jpa.adapter;

import com.foodcourt.plaza_service.domain.model.Page;
import com.foodcourt.plaza_service.domain.model.PaginationRequest;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.spi.IRestaurantPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.jpa.entity.RestaurantEntity;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.IRestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<Restaurant> listAllRestaurants(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPageNumber(),
                paginationRequest.getPageSize(),
                Sort.by("name").ascending()
        );

        org.springframework.data.domain.Page<RestaurantEntity> springPage = restaurantRepository.findAll(pageable);

        List<Restaurant> domainRestaurants = springPage.getContent()
                .stream()
                .map(restaurantEntityMapper::toRestaurant)
                .collect(Collectors.toList());

        return new Page<>(
                domainRestaurants,
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.getNumber(),
                springPage.getSize()
        );
    }
}
