package bfst19.osmdrawing.model.parsing;

import java.util.*;

public class OSMRelation {
	private Collection<OSMWay> list = new ArrayList<>();
	private Map<String, String> tags = new HashMap<>();

	public void addTag(String key, String value) {
		tags.put(key, value);
	}

	public Collection<OSMWay> getList() {
		return list;
	}

	public void add(OSMWay item) {
		list.add(item);
	}

	public void merge() {
		Collection<OSMWay> oldList =  oldMerge(list);
		Collection<OSMWay> newList =  newMerge(list);
		if (oldList.size() > newList.size()){
			list = newList;
		}
		else {
			list = oldList;
		}
	}

	private Collection<OSMWay> oldMerge(Collection<OSMWay> list) {
		//Currently produces two equal lists it appears.
		/*
		Check for each road if it connects in the end ofr the start.
		*/
		Map<OSMNode, OSMWay> pieces = new HashMap<>();
		for (OSMWay way : list) {
			OSMWay res = new OSMWay(0);
			OSMWay before = pieces.remove(way.getFirst());
			if (before != null) {
				pieces.remove(before.getFirst());
				for (int i = 0; i < before.size() - 1; i++) {
					res.add(before.get(i));
				}
			}
			res.addAll(way);
			OSMWay after = pieces.remove(way.getLast());
			if (after != null) {
				pieces.remove(after.getLast());
				for (int i = 1; i < after.size(); i++) {
					res.add(after.get(i));
				}
			}
			pieces.put(res.getFirst(), res);
			pieces.put(res.getLast(), res);
		}
		return pieces.values();
	}

	public Collection<OSMWay> newMerge(Collection<OSMWay> list){
		Map<OSMNode, OSMWay> piecesStarts = new HashMap<>();
		Map<OSMNode, OSMWay> piecesEnds = new HashMap<>();
		for (OSMWay way : list){
			OSMWay res = new OSMWay(0);
			if (piecesStarts.containsKey(way.getFirst()) && piecesEnds.containsKey(way.getLast()) && false){
				throw new RuntimeException("A way with duplicate ending points existed in " + this);
			}
			if (piecesStarts.containsKey(way.getFirst()) || piecesEnds.containsKey(way.getLast())){
				way.reverse();
			}
			if (piecesEnds.containsKey(way.getFirst())){
				OSMWay before = piecesEnds.remove(way.getFirst());
				piecesStarts.remove(before.getFirst());
				addNormal(res, before);
			}
			addNormal(res, way);
			if (piecesStarts.containsKey(way.getLast())){
				OSMWay after = piecesStarts.remove(way.getLast());
				piecesEnds.remove(after.getLast());
				addNormal(res, after);
			}
			piecesStarts.put(res.getFirst(), res);
			piecesEnds.put(res.getLast(), res);
		}
		return piecesEnds.values();
	}

	private void addNormal(OSMWay res, OSMWay addition) {
		for (int i = 0; i < addition.size() - 1; i++) {
			res.add(addition.get(i));
		}
	}

	private void addInReverse(OSMWay res, OSMWay addition) {
		for (int i = addition.size() - 1; i >= 0 ; i--) {
			res.add(addition.get(i));
		}
	}


	public boolean hasMembers() {
		return list.size() > 0;
	}
}
