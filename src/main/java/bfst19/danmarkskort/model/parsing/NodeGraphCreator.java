package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.DrawableModel;
import bfst19.danmarkskort.model.PolyRoad;

import java.util.*;

public class NodeGraphCreator {
	DrawableModel drawableModel;
	private List<OSMRoadNode> roadNodes = new ArrayList<>();
	private Set<OSMRoadWay> OSMRoads = new HashSet<>();
	private Map<OSMRoadWay, PolyRoad> roadWaysToPolyRoads = new HashMap<>();
	private Map<PolyRoad, Integer> polyRoadToIntegers = new HashMap<>();


	public NodeGraphCreator (DrawableModel drawableModel){
		this.drawableModel = drawableModel;
	}



	public void initPolyRoadConnections() {
		for (OSMRoadNode node : roadNodes) {
			OSMRoads.addAll(node.getConnections());
		}
		OSMRoads.addAll(splitWays());
		createPolyRoadsFromOSMRoads();
		fillPolyRoadsIntoArray();
		initializeConnections();
	}

	private void initializeConnections() {
		for (OSMRoadWay way : roadWaysToPolyRoads.keySet()) {
			PolyRoad road = roadWaysToPolyRoads.get(way);
			OSMRoadNode first = (OSMRoadNode) way.getFirst();
			OSMRoadNode last = (OSMRoadNode) way.getLast();
			for (OSMRoadWay connectedWay : first.getConnections()) {
				PolyRoad otherRoad = roadWaysToPolyRoads.get(connectedWay);
				if (road != otherRoad){
					road.addConnectionToFirst(otherRoad);
				}
			}
			for (OSMRoadWay connectedWay : last.getConnections()) {
				PolyRoad otherRoad = roadWaysToPolyRoads.get(connectedWay);
				if (road != otherRoad){
					road.addConnectionToLast(otherRoad);
				}
			}
		}
	}

	private void fillPolyRoadsIntoArray() {
		PolyRoad.allPolyRoads = new PolyRoad[roadWaysToPolyRoads.values().size()];
		int i = 0;
		for (PolyRoad road : roadWaysToPolyRoads.values()) {
			polyRoadToIntegers.put(road, i);
			PolyRoad.allPolyRoads[i] = road;
			road.setIndex(i);
			i++;
		}
	}

	private void createPolyRoadsFromOSMRoads() {
		for (OSMRoadWay way : OSMRoads) {
			PolyRoad newRoad = new PolyRoad(way);
			roadWaysToPolyRoads.put(way, newRoad);
			drawableModel.add(way.getType(), newRoad);
		}
	}

	private List<OSMRoadWay> splitWays() {
		List<OSMRoadWay> toBeAdded = new ArrayList<>();
		for (OSMRoadWay way : OSMRoads) {
			OSMRoadWay newWay = way;
			while (newWay != null) {
				newWay = newWay.splitIfNeeded();
				if (newWay != null) {
					toBeAdded.add(newWay);
				}
			}
		}
		return toBeAdded;
	}


	public void addRoad(OSMRoadWay currentWay) {
		OSMRoads.add(currentWay);
	}

	public void addRoadNode(OSMRoadNode newNode) {
		roadNodes.add(newNode);
	}
}
