package com.foodcourt.plaza_service.domain.utils.constants;

public class DomainConstants {
    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String USER_NOT_AN_OWNER_MESSAGE = "El idPropietario proporcionado no corresponde a un usuario con rol Propietario";

    public static final String NIT_FIELD_MUST_BE_NUMERIC_MESSAGE = "El campo NIT debe ser numérico";
    public static final String PHONE_FIELD_INVALID_MESSAGE = "El teléfono debe tener un máximo de 13 caracteres y puede empezar con el símbolo '+'";
    public static final String NAME_FIELD_NOT_ONLY_NUMBERS_MESSAGE = "El nombre del restaurante no puede contener solo números";

    public static final String RESTAURANT_NOT_FOUND_MESSAGE = "Restaurante no encontrado";
    public static final String NOT_RESTAURANT_OWNER_MESSAGE = "Propietario del restaurante no coincide";

    public static final String PRICE_DISH_NOT_VALID_MESSAGE = "El precio del plato debe ser mayor a cero";

    public static final String CATEGORY_NOT_FOUND_MESSAGE = "La categoría especificada no fue encontrada";

    public static final String DISH_NOT_FOUND_MESSAGE = "El plato no se ha encontrado";

    public static final String CLIENT_HAS_AN_ORDER_MESSAGE = "El cliente tiene un pedido en proceso";

    public static final String ORDER_CANNOT_BE_ASSIGNED_MESSAGE = "El pedido no puede ser asignado porque no está en estado PENDIENTE.";
    public static final String ORDER_NOT_FOUND_MESSAGE = "Orden no encontrada";
    public static final String ORDER_IS_NOT_IN_PREPARATION_MESSAGE = "El pedido no puede ser notificado porque no está en estado 'EN PREPARACION'.";
    public static final String ORDER_READY_MESSAGE = "El pedido esta listo, puede reclamarlo con el pin ";
    public static final String ORDER_IS_NOT_READY_MESSAGE = "El pedido no puede ser entregado porque no está en estado 'LISTO'.";
    public static final String INVALID_PIN_MESSAGE = "El PIN de seguridad proporcionado es incorrecto.";
    public static final String ORDER_CAN_NOT_BE_CANCELED_MESSAGE = "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse.";
    public static final String USER_CAN_NOT_CANCEL_ORDER_MESSAGE = "No tienes permiso para cancelar este pedido.";

    public static final int MINIMUM_PRICE = 0;
    public static final String PIN_FORMAT = "%04d";
    public static final int PIN_SIZE = 10000;
    public static final String STATUS_PENDING = "PENDIENTE";
    public static final String STATUS_IN_PREPARATION = "EN_PREPARACION";
    public static final String STATUS_READY = "LISTO";
    public static final String STATUS_DELIVERED = "ENTREGADO";
    public static final String STATUS_CANCELED = "CANCELADO";

    public static final String NUMERIC_REGEX = "^\\d+$";
    public static final String PHONE_REGEX = "^\\+?\\d{1,12}$";
    public static final int MAX_PHONE_LENGTH = 13;
}
