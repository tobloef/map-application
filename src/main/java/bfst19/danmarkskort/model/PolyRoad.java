package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadWay;

import java.util.HashSet;
import java.util.Set;

public class PolyRoad extends Polyline{
	private double speedLimit;
	private Set<PolyRoad> firstConnections;
	private Set<PolyRoad> lastConnections;

	public PolyRoad(OSMRoadWay way) {
		super(way);
		firstConnections = new HashSet<>();
		lastConnections = new HashSet<>();
		this.speedLimit = way.getSpeedLimit();
	}

	public void addConnectionToFirst(PolyRoad polyRoad) {
		firstConnections.add(polyRoad);
	}
	public void addConnectionTolast(PolyRoad polyRoad) {
		lastConnections.add(polyRoad);
	}
}
