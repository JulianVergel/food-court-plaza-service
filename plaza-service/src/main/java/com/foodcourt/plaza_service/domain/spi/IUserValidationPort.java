package com.foodcourt.plaza_service.domain.spi;

public interface IUserValidationPort {
    boolean isUserOwner(Long userId);
}
