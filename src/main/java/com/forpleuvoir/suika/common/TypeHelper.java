package com.forpleuvoir.suika.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.common
 * @ClassName TypeHelper
 * @author forpleuvoir
 * @CreateTime 2020/10/19 17:34
 * @Description
 */
public class TypeHelper {
    public static <T> T as(Object value, Class<T> clazz) {
        return clazz.isInstance(value) ? (T) value : null;
    }

    public static <T> T as(Object value, Class<T> clazz, Supplier<T> defaultValueFunc) {
        return clazz.isInstance(value) ? (T) value : defaultValueFunc.get();
    }

    public static <T> void doIfType(Object value, Class<T> clazz, Consumer<T> consumer) {
        T typedValue = as(value, clazz);
        if (typedValue == null) {
            return;
        }
        consumer.accept(typedValue);
    }

    public static int combineHashCodes(int... hashCodes) {
        final int prime = 31;
        int result = 0;
        for (int hashCode : hashCodes) {
            result = prime * result + hashCode;
        }
        return result;
    }
}
