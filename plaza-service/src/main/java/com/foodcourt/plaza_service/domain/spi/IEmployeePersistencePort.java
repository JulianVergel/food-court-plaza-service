package com.foodcourt.plaza_service.domain.spi;

public interface IEmployeePersistencePort {
    Long findRestaurantIdByEmployeeId(Long employeeId);
}
