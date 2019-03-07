package bfst19.osmdrawing.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.LongSupplier;

public class LongIndex<T extends LongSupplier> {
	private ArrayList<T> elms = new ArrayList<>();
	private boolean sorted = false;

	public void add(T elm) {
		elms.add(elm);
		sorted = false;
	}

	public T get(long ref) {
		if (!sorted) {
			elms.sort(Comparator.comparing(T::getAsLong));
			sorted = true;
		}
		int lo = 0;
		int hi = elms.size();
		while (hi - lo > 1) {
			int mi = lo + (hi - lo) / 2;
			if (ref < elms.get(mi).getAsLong()) {
				hi = mi;
			} else {
				lo = mi;
			}
		}
		T elm = elms.get(lo);
		if (elm.getAsLong() == ref) {
			return elm;
		} else {
			return null;
		}
	}
}
