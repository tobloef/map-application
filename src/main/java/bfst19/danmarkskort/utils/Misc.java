package bfst19.danmarkskort.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static String internIfNotNull(String str) {
        if (str != null) {
            str = str.intern();
        }
        return str;
    }

    public static List<String> trimList(List<String> list) {
        return list.stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

	public static void arrayCopy(int[] connections, int[] tempArray) {
		System.arraycopy(connections, 0, tempArray, 0, connections.length);
	}
}
