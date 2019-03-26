package bfst19.osmdrawing.model;

import java.util.List;

public class OSMRoadNode extends OSMNode{
	List<Connection> connections;

	public OSMRoadNode(long id, float lon, float lat, List<Connection> connections) {
		super(id, lon, lat);
		this.connections = connections;
	}

	public List<Connection> getConnections() {
		return connections;
	}

	public void addNode(Connection connection) {
		connections.add(connection);
	}
}
