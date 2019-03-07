package bfst19.osmdrawing.Model;

import java.util.ArrayList;
import java.util.function.LongSupplier;

public class OSMWay extends ArrayList<OSMNode> implements LongSupplier {
	long id;

	public OSMWay(long id) {
		this.id = id;
	}

	@Override
	public long getAsLong() {
		return id;
	}

	public OSMNode getFirst() {
		return get(0);
	}

	public OSMNode getLast() {
		return get(size()-1);
	}
}
