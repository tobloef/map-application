package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OSMRoadNode extends OSMNode{
	private List<Connection> connections;

	public OSMRoadNode(OSMNode node) {
		super(node.getAsLong(), node.getLon(), node.getLat());
		connections = new ArrayList<>();
	}

	public List<Connection> getConnections() {
		return connections;
	}

	public void addConnection(Connection newConnection) {
		for (Connection connection : connections) {
			if (connection.equals(newConnection)) return;
		}
		connections.add(newConnection);
	}

	public int getConnectionAmount() {
		return connections.size();
	}

	public Connection getOtherConnection(Connection origin) {
		for (Connection connection : connections) {
			if (connection != origin) {
				return connection;
			}
		}
		return null;
	}

	public List<OSMRoadNode> getConnectedNodes() {
		List<OSMRoadNode> results = new ArrayList();
		for (Connection connection : connections) {
			results.add(getNodeFromConnection(connection));
		}
		return results;
	}

	public boolean isConnected(OSMRoadNode otherNode) {
		return getConnectedNodes().contains(otherNode) || this == otherNode;
	}

	public boolean isConnected(OSMRoadNode otherNode, int depth, Set<OSMRoadNode> triedNodes) {
		if (isConnected(otherNode)) {
			return true;
		}
		if (depth <= 0) {
			return false;
		}
		triedNodes.add(this);
		for (OSMRoadNode node : getConnectedNodes()) {
			if (!triedNodes.contains(node) && node.isConnected(otherNode, depth - 1, triedNodes)) {
				return true;
			}
		}
		return false;
	}

	public void removeConnectionToNode(OSMRoadNode removedNode) {
		for (Connection connection : connections) {
			if (getNodeFromConnection(connection) == removedNode) {
				connections.remove(connection);
				break;
			}
		}
	}

	public String toString() {
		return "" + getAsLong();
	}

	public OSMRoadNode getNodeFromConnection(Connection connection) {
		return connection.getOtherNode(this);
	}
}
