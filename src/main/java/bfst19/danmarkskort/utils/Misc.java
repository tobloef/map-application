package bfst19.danmarkskort.utils;

import java.util.Map;
import java.util.function.Consumer;

public class Misc {
    public static <T> T pickNotNull(T first, T second) {
        if (first != null) {
            return first;
        }
        return second;
    }

    public static <T> T pickNotNull(T first, T second, T third) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }
        return third;
    }

    public static String getWithFallback(Map<String, String> map, String[] keys) {
        for (String key : keys) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }
}
