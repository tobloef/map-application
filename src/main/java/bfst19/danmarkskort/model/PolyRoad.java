package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadNode;
import bfst19.danmarkskort.model.parsing.OSMRoadWay;

import java.util.*;

public class PolyRoad extends Polyline{
	private int index;
	private double speedLimit;
	private Set<Integer> firstConnections;
	private Set<Integer> lastConnections;
	public static PolyRoad[] allPolyRoads;

	public PolyRoad(OSMRoadWay way) {
		super(way);
		firstConnections = new HashSet<>();
		lastConnections = new HashSet<>();
		this.speedLimit = way.getSpeedLimit();
		index = -1;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "" + index;
	}

	public Set<PolyRoad> getOtherConnections(PolyRoad origin) {
		if (firstConnections.contains(origin.getIndex())) {
			return getLastConnections();
		}
		if (lastConnections.contains(origin.getIndex())) {
			return getFirstConnections();
		}
		return new HashSet<>(getAllConnections());
		//throw new IllegalArgumentException("This road is not connected to specified road");
	}

	public double getLength() {
		double result = 0;
		for (int i = 0; i < coords.length - 2; i += 2) {
			result += findDistanceBetween(coords[i], coords[i+1], coords[i+2], coords[i+3]);
		}
		return result;
	}

	private double findDistanceBetween(double x1, double y1, double x2, double y2) {
		double deltaX = x1 - y1;
		double deltaY = x2 - y2;
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}
}
