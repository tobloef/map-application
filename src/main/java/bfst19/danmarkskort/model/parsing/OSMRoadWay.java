package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.WayType;

public class OSMRoadWay extends OSMWay {
	private double speedLimit;
	private WayType type;

	public OSMRoadWay(OSMWay oldWay, double speedLimit, WayType type) {
		super(oldWay.id);
		list = oldWay.getNodes();
		this.speedLimit = speedLimit;
		this.type = type;

	}

	public double getSpeedLimit() {
		return speedLimit;
	}
}
