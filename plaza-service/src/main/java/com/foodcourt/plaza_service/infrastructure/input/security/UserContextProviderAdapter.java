package com.foodcourt.plaza_service.infrastructure.input.security;

import com.foodcourt.plaza_service.domain.spi.IUserContextProviderPort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserContextProviderAdapter implements IUserContextProviderPort {

    @Override
    public Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return jwt.getClaim("id");
        }

        // Retornar null o lanzar una excepción si el principal no es del tipo esperado
        return null;
    }

    @Override
    public String getAuthenticatedUserEmail() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getSubject(); // El 'subject' del token es el email
    }
}
