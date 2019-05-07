package bfst19.danmarkskort.utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Misc {
    public static <T> T pickNotNull(T first, T second) {
        if (first != null) {
            return first;
        }
        return second;
    }

    public static String getWithFallback(Map<String, String> map, String[] keys) {
        for (String key : keys) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }

    public static String internIfNotNull(String str) {
        if (str != null) {
            str = str.intern();
        }
        return str;
    }
}
