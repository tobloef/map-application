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

	public boolean equals(Connection connection) {
		if (node == connection.getNode() && Math.abs(distance - connection.getDistance()) < 0.01 && Math.abs(speedLimit - connection.getSpeedLimit()) < 0.01) return true;
		return false;
	}
}
