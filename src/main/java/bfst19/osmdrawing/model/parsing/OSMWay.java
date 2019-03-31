package bfst19.osmdrawing.model.parsing;

import java.util.*;
import java.util.function.LongSupplier;

public class OSMWay implements LongSupplier {
	List<OSMNode> list = new ArrayList<>();
	long id;
	public Map<String, String> tags = new HashMap<>();

	public OSMWay(long id) {
		this.id = id;
	}

	@Override
	public long getAsLong() {
		return id;
	}

	public OSMNode getFirst() {
		return list.get(0);
	}

	public OSMNode getLast() {
		return list.get(list.size()-1);
	}

	public int size() {
		return list.size();
	}

	public OSMNode get(int i) {
		return list.get(i);
	}

	public void add(OSMNode osmNode) {
		list.add(osmNode);
	}

	public void addAll(OSMWay way) {
		for (OSMNode node : way.getNodes()) {
			add(node);
		}
	}

	public List<OSMNode> getNodes() {
		return list;
	}

	public void reverse() {
		Collections.reverse(list);
	}

	public boolean contains(OSMNode node) {
		return list.contains(node);
	}
}
