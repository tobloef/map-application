package bfst19.danmarkskort.model.parsing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

public class OSMWay implements LongSupplier {
	List<OSMNode> list = new ArrayList<>();
	long id;

	public OSMWay(long id) {
		this.id = id;
	}

	public OSMWay(long id, List<OSMNode> list) {
		this.id = id;
		this.list = list;
	}

	@Override
	public long getAsLong() {
		return id;
	}

	public OSMNode getFirst() {
		if (list.size() == 0) {
			return null;
		}
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


	public void addAllAtStart(OSMWay duplicate) {
		duplicate.addAll(this);
		this.list = duplicate.getNodes();
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

	public void removeDuplicates() {
		list = list.stream().distinct().collect(Collectors.toList());
	}

}
