package com.foodcourt.plaza_service.infrastructure.output.jpa.mapper;


import com.foodcourt.plaza_service.domain.model.PaginationResponse;
import java.util.List;
import java.util.function.Function;

public final class PaginationMapper {
    private PaginationMapper() {}

    public static <S, T> PaginationResponse<T> toDomainPage(org.springframework.data.domain.Page<S> springPage, Function<S, T> mapper) {
        List<T> content = springPage.getContent()
                .stream()
                .map(mapper)
                .toList();

        return new PaginationResponse<>(
                content,
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.getNumber(),
                springPage.getSize()
        );
    }
}
