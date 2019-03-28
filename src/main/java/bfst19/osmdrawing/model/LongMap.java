package bfst19.osmdrawing.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.LongSupplier;

public class LongMap<T extends LongSupplier> {
	private ArrayList<T> elements = new ArrayList<>();
	private boolean sorted = false;

	public void add(T element) {
		elements.add(element);
		sorted = false;
	}

	public T get(long ref) {
		T element = elements.get(findIndex(ref));
		if (element.getAsLong() == ref) {
			return element;
		} else {
			return null;
		}
	}

	public void replace(T element) {
		int index = findIndex(element.getAsLong());
		if (elements.get(index).getAsLong() == element.getAsLong()) {
			elements.set(index, element);
		}
		else {
			add(element);
		}
	}

	private int findIndex(long ref) {
		if (!sorted) {
			elements.sort(Comparator.comparing(T::getAsLong));
			sorted = true;
		}
		int low = 0;
		int high = elements.size();
		while (high - low > 1) {
			int middle = low + (high - low) / 2;
			if (ref < elements.get(middle).getAsLong()) {
				high = middle;
			} else {
				low = middle;
			}
		}
		return low;
	}


}
