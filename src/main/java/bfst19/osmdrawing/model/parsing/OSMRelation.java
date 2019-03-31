package bfst19.osmdrawing.model.parsing;

import java.util.*;

public class OSMRelation {
	private Collection<OSMWay> list = new ArrayList<>();

	public Collection<OSMWay> getList() {
		return list;
	}

	public void add(OSMWay item) {
		list.add(item);
	}

	public void merge() {
		list =  newMerge(list);
	}

	private void printOverLaps(Collection<OSMWay> list) {
		int correctOverlaps = 0;
		int wrongOverLaps = 0;
		for (OSMWay way : list){
			for (OSMWay otherWay : list){
				if (way == otherWay) {
					continue;
				}
				if (way.getFirst() == otherWay.getLast()){
					correctOverlaps++;
				}
				if(way.getFirst() == otherWay.getFirst() || way.getLast() == otherWay.getLast()){
					wrongOverLaps++;
				}
			}
		}
		System.out.println("Number of ways: " + list.size());
		System.out.println("Correct: " + correctOverlaps + " Wrong: " + wrongOverLaps);
	}

	private static Collection<OSMWay> newMerge(Collection<OSMWay> list){
		Map<OSMNode, OSMWay> piecesStarts = new HashMap<>();
		Map<OSMNode, OSMWay> piecesEnds = new HashMap<>();
		for (OSMWay way : list){
			OSMWay res = new OSMWay(0);
			if (piecesStarts.containsKey(way.getFirst()) || piecesEnds.containsKey(way.getLast())){
				way.reverse();
			}
			OSMWay before = piecesEnds.remove(way.getFirst());
			if (before != null){
				piecesStarts.remove(before.getFirst());
				addNormal(res, before);
			}
			addNormal(res, way);
			OSMWay after = piecesStarts.remove(way.getLast());
			if (after != null){
				piecesEnds.remove(after.getLast());
				addNormal(res, after);
			}
			piecesStarts.put(res.getFirst(), res);
			piecesEnds.put(res.getLast(), res);
		}
		return piecesEnds.values();
	}

	private static void addNormal(OSMWay res, OSMWay addition) {
		for (int i = 0; i < addition.size(); i++) {
			res.add(addition.get(i));
		}
	}

	public boolean hasMembers() {
		return list.size() > 0;
	}
}
