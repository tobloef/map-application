package bfst19.danmarkskort.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class BinarySearch {
    /**
     * Performs binary search on a sorted list to find the index of a given element.
     *
     * @param <T>        Type of the object in the list.
     * @param list       A sorted list to search in.
     * @param target     Object to find in the list.
     * @param comparator Comparator for the objects.
     * @return Index of the first matching item found, -1 if not found.
     */
    public static <T> int search(List<T> list, T target, Comparator<T> comparator) {
        return search(list, target, Function.identity(), comparator);
    }

    /**
     * Performs binary search on a sorted list to find the index of a given element.
     * Allows for searching lists of object of a different type to the target's type.
     *
     * @param <T1>       The type of object in the list.
     * @param <T2>       The type of object to compare to.
     * @param list       A sorted list to search in.
     * @param target     Object to find in the list.
     * @param getter     Function for getting the object to compare from an item in the list.
     * @param comparator Comparator for the objects.
     * @return Index of the first matching item found, -1 if not found.
     */
    public static <T1, T2> int search(List<T1> list, T2 target, Function<T1, T2> getter, Comparator<T2> comparator) {
        int low = 0;
        int high = list.size();
        int middle = -1;
        while (true) {
            int newMiddle = low + (high - low) / 2;
            if (newMiddle == middle) {
                break;
            }
            middle = newMiddle;
            T2 actual = getter.apply(list.get(middle));
            int compareResult = comparator.compare(actual, target);
            if (compareResult == 0) {
                return middle;
            } else if (compareResult > 0) {
                high = middle;
            } else {
                low = middle;
            }
        }
        return -1;
    }
}
