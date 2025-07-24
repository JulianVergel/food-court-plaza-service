package com.foodcourt.plaza_service.domain.spi;

public interface IUserPersistencePort {
    String findUserPhoneById(Long userId);
}
