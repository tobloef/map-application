package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadWay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolyRoad extends Polyline{
	private int index;
	private double speedLimit;
	private double distance = -1;
	private Set<Integer> firstConnections;
	private Set<Integer> lastConnections;
	public static PolyRoad[] allPolyRoads;

	public PolyRoad(OSMRoadWay way) {
		super(way);
		firstConnections = new HashSet<>();
		lastConnections = new HashSet<>();
		this.speedLimit = way.getSpeedLimit();
		index = -1; // todo should test how much performance slows if this is set on startup. It's a lot of sqrt calls
	}
	public void setIndex(int index) {
		this.index = index;
	}



	public void addConnectionToFirst(PolyRoad road) {
		firstConnections.add(road.getIndex());
	}

	public void addConnectionTolast(PolyRoad road) {
		lastConnections.add(road.getIndex());
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	private double findDistanceBetween(double x1, double y1, double x2, double y2) {
		double deltaX = x1 - x2;
		double deltaY = y1 - y2;
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}

	public double getLength() {
		if (distance > -1) {
			return distance;
		}
		else {
			distance = 0;
			for (int i = 0; i < coords.length-2; i += 2) {
				distance += findDistanceBetween(coords[i], coords[i+1], coords[i+2], coords[i+3]);
			}
			return distance;
		}
	}

	public Set<PolyRoad> getOtherConnections(PolyRoad origin) {
		int originIndex = origin.getIndex();
		if (firstConnections.contains(originIndex)) {
			return getLastConnections();
		}
		if (lastConnections.contains(originIndex)) {
			return getFirstConnections();
		}
		throw new RuntimeException("Error: This road has no connection to the specified road.");
	}

	public int getIndex() {
		return index;
	}

	public Set<PolyRoad> getFirstConnections() {
		Set<PolyRoad> result = new HashSet<>();
		for (Integer i : firstConnections) {
			result.add(allPolyRoads[i]);
		}
		return result;
	}

	public Set<PolyRoad> getLastConnections() {
		Set<PolyRoad> result = new HashSet<>();
		for (Integer i : lastConnections) {
			result.add(allPolyRoads[i]);
		}
		return result;
	}

	public List<PolyRoad> getAllConnections() {
		List<PolyRoad> result = new ArrayList<>();
		result.addAll(getFirstConnections());
		result.addAll(getLastConnections());
		return result;
	}

	@Override
	public String toString() {
		return "" + index;
	}
}
