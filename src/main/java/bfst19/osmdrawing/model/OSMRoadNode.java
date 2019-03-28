package bfst19.osmdrawing.model;

import java.util.ArrayList;
import java.util.List;

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

	public void addConnection(Connection connection) {
		connections.add(connection);
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

	public void removeConnectionToNode(OSMRoadNode node) {
		for (Connection connection : connections) {
			if (connection.getNode() == node) {
				connections.remove(connection);
				break;
			}
		}
	}
}
