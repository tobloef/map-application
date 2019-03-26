package bfst19.osmdrawing.model;

public class Connection {
	private OSMNode node;
	private double distance;
	private double speedLimit;

	public Connection(OSMNode node, double distance, double speedLimit) {
		this.node = node;
		this.distance = distance;
		this.speedLimit = speedLimit;
	}

	public OSMNode getNode() {
		return node;
	}

	public double getDistance() {
		return distance;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}
}
