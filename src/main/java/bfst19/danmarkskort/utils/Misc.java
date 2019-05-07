package bfst19.danmarkskort.utils;

import java.util.Map;
import java.util.stream.StreamSupport;

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

    public static long countIterable(Iterable iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).count();
    }
}
