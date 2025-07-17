package com.foodcourt.plaza_service.infrastructure.output.jpa.adapter;

import com.foodcourt.plaza_service.domain.model.Category;
import com.foodcourt.plaza_service.domain.spi.ICategoryPersistencePort;
import com.foodcourt.plaza_service.infrastructure.output.jpa.mapper.ICategoryEntityMapper;
import com.foodcourt.plaza_service.infrastructure.output.jpa.repository.ICategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryJpaRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id).map(categoryEntityMapper::toCategory);
    }
}
