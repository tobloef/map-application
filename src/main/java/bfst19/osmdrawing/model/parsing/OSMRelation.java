package bfst19.osmdrawing.model.parsing;

import java.util.*;

public class OSMRelation {
	private Collection<OSMWay> list = new ArrayList<>();
	private Map<String, String> tags = new HashMap<>();

	public void addTag(String key, String value){
		tags.put(key, value);
	}

	public Collection<OSMWay> getList() {
		return list;
	}
	public void add(OSMWay item) {
		list.add(item);
	}

	public void merge() {
		//Currently produces two equal lists it appears.
		/*
		Check for each road if it connects in the end ofr the start.
		*/
		Map<OSMNode,OSMWay> pieces = new HashMap<>();
		for (OSMWay way : list) {
			OSMWay res = new OSMWay(0);
			OSMWay before = pieces.remove(way.getFirst());
			if (before != null) {
				pieces.remove(before.getFirst());
				for (int i = 0 ; i < before.size() - 1 ; i++) {
					res.add(before.get(i));
				}
			}
			res.addAll(way);
			OSMWay after = pieces.remove(way.getLast());
			if (after != null) {
				pieces.remove(after.getLast());
				for (int i = 1 ; i < after.size() ; i++) {
					res.add(after.get(i));
				}
			}
			pieces.put(res.getFirst(), res);
			pieces.put(res.getLast(), res);
		}
		list = pieces.values();
	}

	public boolean hasMembers() {
		return list.size() > 0;
	}
}
