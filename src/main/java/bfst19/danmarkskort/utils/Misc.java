package bfst19.danmarkskort.utils;

public class Misc {
    public static <T> T pickNotNull(T first, T second) {
        if (first != null) {
            return first;
        }
        return second;
    }
}
