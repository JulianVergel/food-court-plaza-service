package com.foodcourt.plaza_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Page<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        List<U> convertedContent = this.content.stream()
                .map(converter)
                .collect(Collectors.toList());
        return new Page<>(convertedContent, this.totalElements, this.totalPages, this.pageNumber, this.pageSize);
    }
}
