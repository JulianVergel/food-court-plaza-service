package com.foodcourt.plaza_service.domain.utils.validations;

import com.foodcourt.plaza_service.domain.model.Restaurant;

import static com.foodcourt.plaza_service.domain.utils.constants.DomainConstants.*;

public class RestaurantValidator {
    private RestaurantValidator() {}

    public static void validateName(String name) {
        if (name.matches("^\\d+$")) {
            throw new IllegalArgumentException(NAME_FIELD_NOT_ONLY_NUMBERS_MESSAGE);
        }
    }

    public static void validateNit(String nit) {
        if (!nit.matches("^\\d+$")) {
            throw new IllegalArgumentException(NIT_FIELD_MUST_BE_NUMERIC_MESSAGE);
        }
    }

    public static void validatePhone(String phone) {
        if (!phone.matches("^\\+?\\d{1,12}$") || phone.length() > 13) {
            throw new IllegalArgumentException(PHONE_FIELD_INVALID_MESSAGE);
        }
    }

    public static void validateRestaurantRequest(Restaurant restaurant) {
        validateNit(restaurant.getNit());
        validatePhone(restaurant.getPhone());
        validateName(restaurant.getName());
    }
}
