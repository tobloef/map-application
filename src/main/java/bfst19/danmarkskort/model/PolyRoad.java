package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadWay;
import bfst19.danmarkskort.model.parsing.OSMWay;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolyRoad extends Polyline{
	private double speedLimit;
	private Set<PolyRoad> firstConnections;
	private Set<PolyRoad> lastConnnections;

	public PolyRoad(OSMRoadWay way, double speedLimit) {
		super(way);
		firstConnections = new HashSet<>();
		lastConnnections = new HashSet<>();
		this.speedLimit = speedLimit;
	}

	public void init() {

	}
}
