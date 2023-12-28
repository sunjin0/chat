package com.example.chatuser.util;

public class ParamUtils {
    public static boolean isNull(Object parameter) {
        return parameter == null;
    }

    public static boolean isBlank(String parameter) {
        return parameter.trim().isEmpty();
    }

    public static boolean greaterThanZero(int parameter) {
        return parameter > 0;
    }
}

