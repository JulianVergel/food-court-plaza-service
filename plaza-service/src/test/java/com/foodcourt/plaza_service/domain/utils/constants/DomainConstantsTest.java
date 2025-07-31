package com.foodcourt.plaza_service.domain.utils.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DomainConstantsTest {
    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<DomainConstants> constructor = DomainConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, constructor::newInstance,
                "Se esperaba que el constructor lanzara una excepción");
    }
}
