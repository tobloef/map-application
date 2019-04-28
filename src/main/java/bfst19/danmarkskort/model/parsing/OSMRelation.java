package bfst19.danmarkskort.model.parsing;

import java.util.*;
import java.util.function.LongSupplier;

public class OSMRelation implements LongSupplier {
	private List<OSMWay> list = new ArrayList<>();
	public String debugName;
	long id;

	public OSMRelation(long id) {
		this.id = id;
	}

	public Collection<OSMWay> getList() {
		return list;
	}

	public void add(OSMWay item) {
		list.add(item);
	}

	public boolean hasMembers() {
		return list.size() > 0;
	}


	public void merge() {
		list = newMerge(list);
	}

	private static List<OSMWay> newMerge(List<OSMWay> inputList){
		Map<OSMNode, OSMWay> piecesStarts = new HashMap<>();
		Map<OSMNode, OSMWay> piecesEnds = new HashMap<>();
		for (OSMWay currentWay : inputList){
			OSMWay result = new OSMWay(0);
			reverseIfWayIsWrongDirection(piecesStarts, piecesEnds, currentWay);
			//If there is exists a way that is before current way, add that before.
			addWayBefore(piecesStarts, piecesEnds, currentWay, result);
			//add current way to relation
			addNormal(result, currentWay);
			//If there is one after.
			addWayAfter(piecesStarts, piecesEnds, currentWay, result);

			//Some results end up in the wrong order, so they have to merged as a special case.
			mergeWithPriorResults(piecesStarts, piecesEnds, result);

			//Add current result to the map of pieces.
			piecesStarts.put(result.getFirst(), result);
			piecesEnds.put(result.getLast(), result);
		}

		List<OSMWay> finalElements = mergeMaps(piecesEnds);
		//Duplicates stem from nodes existing in both ways that get added together.
		removeDuplicateNodes(finalElements);
		return finalElements;
	}

	//Merge the the results of the two maps, should be the same elements in both, but just to be sure.
	private static List<OSMWay> mergeMaps(Map<OSMNode, OSMWay> piecesEnds) {
		Set<OSMWay> finalElementsSet = new LinkedHashSet<>();
		finalElementsSet.addAll(piecesEnds.values());
		finalElementsSet.addAll(piecesEnds.values());
		return new ArrayList<>(finalElementsSet);
	}

	private static void reverseIfWayIsWrongDirection(Map<OSMNode, OSMWay> piecesStarts, Map<OSMNode, OSMWay> piecesEnds, OSMWay way) {
		if (piecesStarts.containsKey(way.getFirst()) || piecesEnds.containsKey(way.getLast())){
			way.reverse();
		}
	}

	private static void mergeWithPriorResults(Map<OSMNode, OSMWay> piecesStarts, Map<OSMNode, OSMWay> piecesEnds, OSMWay res) {
		mergeResultsBefore(piecesStarts, piecesEnds, res);
		mergeResultsAfter(piecesStarts, piecesEnds, res);
	}

	private static void mergeResultsBefore(Map<OSMNode, OSMWay> piecesStarts, Map<OSMNode, OSMWay> piecesEnds, OSMWay res) {
		if (piecesStarts.containsKey(res.getFirst())){
			OSMWay duplicate = piecesStarts.remove(res.getFirst());
			piecesEnds.remove(duplicate.getLast());
			duplicate.reverse();
			addAtStart(res, duplicate);
		}
	}

	private static void mergeResultsAfter(Map<OSMNode, OSMWay> piecesStarts, Map<OSMNode, OSMWay> piecesEnds, OSMWay res) {
		if (piecesEnds.containsKey(res.getLast())){
			OSMWay duplicate = piecesEnds.remove(res.getLast());
			piecesStarts.remove(duplicate.getFirst());
			duplicate.reverse();
			addNormal(res, duplicate);
		}
	}

	private static void addWayAfter(Map<OSMNode, OSMWay> piecesStarts, Map<OSMNode, OSMWay> piecesEnds, OSMWay way, OSMWay res) {
		OSMWay after = piecesStarts.remove(way.getLast());
		if (after != null){
			piecesEnds.remove(after.getLast());
			addNormal(res, after);
		}
	}

	private static void addWayBefore(Map<OSMNode, OSMWay> piecesStarts, Map<OSMNode, OSMWay> piecesEnds, OSMWay way, OSMWay res) {
		OSMWay before = piecesEnds.remove(way.getFirst());
		if (before != null){
			piecesStarts.remove(before.getFirst());
			addNormal(res, before);
		}
	}

	private static void removeDuplicateNodes(List<OSMWay> pieces) {
		for (OSMWay way : pieces){
			way.removeDuplicates();
		}
	}

	private static void addNormal(OSMWay res, OSMWay addition) {
		res.addAll(addition);
	}

	private static void addAtStart(OSMWay res, OSMWay duplicate) {
		res.addAllAtStart(duplicate);
	}

	public OSMWay getFirst() {
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public long getAsLong() {
		return id;
	}
}
