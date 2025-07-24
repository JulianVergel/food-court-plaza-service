package com.foodcourt.plaza_service.infrastructure.utils.constants;

public class FeignConstants {
    private FeignConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String OWNER_ROLE_NAME = "Propietario";
    public static final String USER_NOT_FOUND_MESSAGE = "Usuario no encontrado ";
    public static final String EMPLOYEE_NOT_FOUND_MESSAGE = "Empleado no encontrado";
    public static final String FEIGN_CLIENT_ERROR_MESSAGE = "Un error ocurrió al validar el usuario ";
}
