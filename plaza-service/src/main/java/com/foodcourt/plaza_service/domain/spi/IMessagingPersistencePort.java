package com.foodcourt.plaza_service.domain.spi;

public interface IMessagingPersistencePort {
    void sendNotification(String phone, String message);
}
