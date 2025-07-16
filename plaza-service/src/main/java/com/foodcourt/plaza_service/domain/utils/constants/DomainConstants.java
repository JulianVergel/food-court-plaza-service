package com.foodcourt.plaza_service.domain.utils.constants;

public class DomainConstants {
    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String USER_NOT_AN_OWNER_MESSAGE = "El idPropietario proporcionado no corresponde a un usuario con rol Propietario";

    public static final String NIT_FIELD_MUST_BE_NUMERIC_MESSAGE = "El campo NIT debe ser numérico";
    public static final String PHONE_FIELD_INVALID_MESSAGE = "El teléfono debe tener un máximo de 13 caracteres y puede empezar con el símbolo '+'";
    public static final String NAME_FIELD_NOT_ONLY_NUMBERS_MESSAGE = "El nombre del restaurante no puede contener solo números";
}
