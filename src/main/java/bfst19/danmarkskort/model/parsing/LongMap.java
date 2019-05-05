package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.utils.BinarySearch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.LongSupplier;

public class LongMap<T extends LongSupplier> {
    private ArrayList<T> elements = new ArrayList<>();
    private boolean sorted = false;

    public void add(T element) {
        elements.add(element);
        sorted = false;
    }

    public T get(long ref) {
        int index = findIndex(ref);
        if (index == -1) {
            return null;
        }
        return elements.get(index);
    }

    public void replace(T element) {
        int index = findIndex(element.getAsLong());
        if (index == -1) {
            add(element);
            return;
        }
        elements.set(index, element);
    }

    private int findIndex(Long ref) {
        Function<T, Long> getter = LongSupplier::getAsLong;
        if (!sorted) {
            elements.sort(Comparator.comparing(getter));
            sorted = true;
        }
        return BinarySearch.search(elements, ref, getter, Long::compareTo);
    }
}
