package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadWay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolyRoad extends Polyline{
	private double speedLimit;
	private Set<Integer> firstConnections;
	private Set<Integer> lastConnections;
	public static PolyRoad[] allPolyRoads;

	public PolyRoad(OSMRoadWay way) {
		super(way);
		firstConnections = new HashSet<>();
		lastConnections = new HashSet<>();
		this.speedLimit = way.getSpeedLimit();
	}

	public void addConnectionToFirst(Integer i) {
		firstConnections.add(i);
	}

	public void addConnectionTolast(Integer i) {
		lastConnections.add(i);
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

	@Override
	public String toString() {
		return "" + index;
	}
}
