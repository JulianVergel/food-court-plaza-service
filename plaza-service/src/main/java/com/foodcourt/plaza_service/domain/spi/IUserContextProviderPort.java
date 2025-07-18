package com.foodcourt.plaza_service.domain.spi;

public interface IUserContextProviderPort {
    Long getAuthenticatedUserId();
}
