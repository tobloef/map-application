package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadNode;
import bfst19.danmarkskort.model.parsing.OSMRoadWay;

import java.util.*;

public class PolyRoad extends Polyline {
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

	public void addConnectionToLast(PolyRoad road) {
		lastConnections.add(road.getIndex());
	}

	public double euclideanDistanceTo(PolyRoad target){
		return findDistanceBetween(getRepresentativeX(), getRepresentativeY(), target.getRepresentativeX(), target.getRepresentativeY());
	}

	public double weightedEuclideanDistanceTo(PolyRoad target){
		//130 km/t på motorveje
		return euclideanDistanceTo(target)/130;
	}

	private static double findDistanceBetween(double x1, double y1, double x2, double y2) {
		double deltaX = x1 - y1;
		double deltaY = x2 - y2;
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
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

	public Set<PolyRoad> getOtherConnections(PolyRoad origin) {
		if (firstConnections.contains(origin.getIndex())) {
			return getLastConnections();
		}
		if (lastConnections.contains(origin.getIndex())) {
			return getFirstConnections();
		}
		return new HashSet<>(getAllConnections());
		//fixme the above is a hack that slows down dijkstra, we should figure out why it happens and fix it so we can use the below instead
		//throw new IllegalArgumentException("This road is not connected to specified road");
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

	public double getLength() {
		double result = 0;
		for (int i = 0; i < coords.length - 2; i += 2) {
			result += findDistanceBetween(coords[i], coords[i+1], coords[i+2], coords[i+3]);
		}
		return result;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public double getWeight() {
		return getLength() / getSpeedLimit();
	}
}
