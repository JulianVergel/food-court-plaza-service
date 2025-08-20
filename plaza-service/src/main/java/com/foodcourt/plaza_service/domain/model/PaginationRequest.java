package com.foodcourt.plaza_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationRequest {
    private int pageNumber;
    private int pageSize;
}
