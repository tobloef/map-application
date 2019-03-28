package bfst19.osmdrawing.model;

public class Connection {
	private OSMRoadNode node;
	private double distance;
	private double speedLimit;

	public Connection(OSMRoadNode node, double distance, double speedLimit) {
		this.node = node;
		this.distance = distance;
		this.speedLimit = speedLimit;
	}

	public OSMRoadNode getNode() {
		return node;
	}

	public double getDistance() {
		return distance;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}
}
