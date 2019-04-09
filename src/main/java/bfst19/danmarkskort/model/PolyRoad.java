package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadWay;
import bfst19.danmarkskort.model.parsing.OSMWay;

import java.util.List;

public class PolyRoad extends Polyline{
	private double speedLimit;
	private List<PolyRoad> firstConnections;
	private List<PolyRoad> lastConnnections;

	public PolyRoad(OSMRoadWay way, double speedLimit) {
		super(way);

		this.speedLimit = speedLimit;
	}
}
