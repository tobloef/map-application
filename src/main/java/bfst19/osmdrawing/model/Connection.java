package bfst19.osmdrawing.model;

public class Connection {
	private OSMRoadNode firstNode, secondNode;
	private double distance, speedLimit;

	public Connection(OSMRoadNode firstNode, OSMRoadNode secondNode, double distance, double speedLimit) {
		this.firstNode = firstNode;
		this.secondNode = secondNode;
		this.distance = distance;
		this.speedLimit = speedLimit;
	}

	public OSMRoadNode getFirstNode() {return firstNode;}

	public OSMRoadNode getSecondNode() {return secondNode;}

	public double getDistance() {
		return distance;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public boolean equals(Connection connection) {
		if (firstNode == connection.getFirstNode() && secondNode == connection.getSecondNode() && Math.abs(distance - connection.getDistance()) < 0.01 && Math.abs(speedLimit - connection.getSpeedLimit()) < 0.01) return true;
		return false;
	}
}
