package bfst19.danmarkskort.model.parsing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
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

	public boolean hasDuplicates() {
		Set<Long> foundLongs = new HashSet<>();
		for (T element : elements) {
			if (foundLongs.contains(element.getAsLong())) return true;
			foundLongs.add(element.getAsLong());
		}
		return false;
	}

	public ArrayList<T> getAll(long ref) {
		int index = findIndex(ref);
		ArrayList<T> results = new ArrayList<>();
		while (elements.get(index).getAsLong() == ref) {
			results.add(elements.get(index));
			index++;
		}
		return results;
	}

	public void deleteDuplicates() {

	}

}
