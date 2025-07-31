package com.foodcourt.plaza_service.infrastructure.input.doc;

public class SwaggerConstants {
    private SwaggerConstants() {}

    public static final String RESPONSE_201_DESCRIPTION = "Recurso creado exitosamente.";
    public static final String RESPONSE_200_DESCRIPTION = "Operación completada exitosamente.";
    public static final String RESPONSE_401_DESCRIPTION = "No autorizado, se requiere token de autenticación válido.";
    public static final String RESPONSE_403_DESCRIPTION = "Acceso denegado.";
    public static final String RESPONSE_404_DESCRIPTION = "Recurso no encontrado.";
    public static final String RESPONSE_409_DESCRIPTION = "Conflicto de estado, el recurso no puede ser modificado.";

    public static final String DISH_CREATE_SUMMARY = "Crear un nuevo plato";
    public static final String DISH_UPDATE_SUMMARY = "Actualizar un plato existente";
    public static final String DISH_ENABLE_DISABLE_SUMMARY = "Habilitar o deshabilitar un plato";
    public static final String DISH_LIST_SUMMARY = "Listar los platos de un restaurante";

    public static final String RESTAURANT_ID_PARAM_DESCRIPTION = "ID del restaurante del que se quieren listar los platos";
    public static final String CATEGORY_ID_PARAM_DESCRIPTION = "ID de la categoría para filtrar los platos (opcional)";
    public static final String PAGE_PARAM_DESCRIPTION = "Número de la página a obtener";
    public static final String SIZE_PARAM_DESCRIPTION = "Número de elementos por página";

    public static final String PAGE_DEFAULT_VALUE = "0";
    public static final String SIZE_DEFAULT_VALUE = "10";

    public static final String ORDER_CREATE_SUMMARY = "Realizar un pedido";
    public static final String ORDER_LIST_SUMMARY = "Listar pedidos por estado";
    public static final String ORDER_ASSIGN_SUMMARY = "Asignarse a un pedido y cambiar estado a 'en preparación'";
    public static final String ORDER_READY_SUMMARY = "Cambiar estado a 'listo' y notificar al cliente";
    public static final String ORDER_DELIVER_SUMMARY = "Cambiar estado a 'Entregado'";
    public static final String ORDER_CANCEL_SUMMARY = "Cancelar un pedido";

    public static final String STATUS_PARAM_DESCRIPTION = "Estado del pedido por el cual filtrar";

    public static final String RESTAURANT_CREATE_SUMMARY = "Crear un nuevo restaurante";
    public static final String RESTAURANT_LIST_SUMMARY = "Listar restaurantes";

    public static final String RESTAURANT_CREATED_DESCRIPTION = "Restaurante creado exitosamente.";
    public static final String RESTAURANTS_LISTED_DESCRIPTION = "Restaurantes listados exitosamente.";
}
