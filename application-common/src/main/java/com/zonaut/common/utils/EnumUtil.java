package com.zonaut.common.utils;

import java.util.Random;

public final class EnumUtil {

    private static final Random RANDOM = new Random();

    private EnumUtil() {
    }

    public static <E extends Enum<E>> E randomEnum(final Class<E> enumClass) {
        final E[] constants = enumClass.getEnumConstants();
        return constants[RANDOM.nextInt(constants.length)];
    }
}
