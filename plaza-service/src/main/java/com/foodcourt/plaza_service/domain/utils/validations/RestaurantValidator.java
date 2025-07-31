package com.foodcourt.plaza_service.domain.utils.validations;

import com.foodcourt.plaza_service.domain.exception.InvalidNitException;
import com.foodcourt.plaza_service.domain.exception.InvalidPhoneException;
import com.foodcourt.plaza_service.domain.exception.InvalidRestaurantNameException;
import com.foodcourt.plaza_service.domain.model.Restaurant;
import com.foodcourt.plaza_service.domain.utils.constants.DomainConstants;


public class RestaurantValidator {
    private RestaurantValidator() {}

    public static void validateName(String name) {
        if (name.matches(DomainConstants.NUMERIC_REGEX)) {
            throw new InvalidRestaurantNameException();
        }
    }

    public static void validateNit(String nit) {
        if (!nit.matches(DomainConstants.NUMERIC_REGEX)) {
            throw new InvalidNitException();
        }
    }

    public static void validatePhone(String phone) {
        if (!phone.matches(DomainConstants.PHONE_REGEX) || phone.length() > DomainConstants.MAX_PHONE_LENGTH) {
            throw new InvalidPhoneException();
        }
    }

    public static void validateRestaurantRequest(Restaurant restaurant) {
        validateNit(restaurant.getNit());
        validatePhone(restaurant.getPhone());
        validateName(restaurant.getName());
    }
}
