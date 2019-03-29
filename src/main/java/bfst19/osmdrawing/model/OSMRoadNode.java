package bfst19.osmdrawing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OSMRoadNode extends OSMNode{
	List<Connection> connections;

	public OSMRoadNode(long id, float lon, float lat, List<Connection> connections) {
		super(id, lon, lat);
		this.connections = connections;
	}

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

	public Connection getOtherConnection(OSMRoadNode node) {
		for (Connection connection : connections) {
			if (connection.getNode() != node) {
				return connection;
			}
		}
		return null;
	}

	public List<OSMRoadNode> getConnectedNodes() {
		List<OSMRoadNode> results = new ArrayList();
		for (Connection connection : connections) {
			results.add(connection.getNode());
		}
		return results;
	}

	public boolean isConnected(OSMRoadNode otherNode) {
		return getConnectedNodes().contains(otherNode) || this == otherNode;
	}

	public boolean isConnected(OSMRoadNode otherNode, int depth, Set<OSMRoadNode> triedNodes) {
		if (isConnected(otherNode)) return true;
		if (depth > 0) {
			triedNodes.add(this);
			for (OSMRoadNode node : getConnectedNodes()) {
				if (!triedNodes.contains(node)) {
					 if(node.isConnected(otherNode, depth - 1, triedNodes)) return true;
				}
			}
		}
		return false;
	}

	public void removeConnectionToNode(OSMRoadNode node) {
		for (Connection connection : connections) {
			if (connection.getNode() == node) {
				connections.remove(connection);
				break;
			}
		}
	}

	public String toString() {
		return "" + getAsLong();
	}
}
