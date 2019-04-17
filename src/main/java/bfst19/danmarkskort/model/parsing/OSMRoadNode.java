package bfst19.danmarkskort.model.parsing;

import java.util.HashSet;
import java.util.Set;

public class OSMRoadNode extends OSMNode{
	private Set<OSMRoadWay> connections;

	public OSMRoadNode(OSMNode node) {
		super(node.getAsLong(), node.getLon(), node.getLat());
		connections = new HashSet<>();
	}

	public void add(OSMRoadWay newConnection) {
		for (OSMRoadWay connection : connections) {
			if (connection.equals(newConnection)) return;
		}
		connections.add(newConnection);
	}

	public int getConnectionAmount() {
		return connections.size();
	}

	public Set<OSMRoadWay> getConnections() {
		return connections;
	}

	public String toString() {
		return "" + getAsLong();
	}

	public void changeConnection(OSMRoadWay old, OSMRoadWay newConnection) {
		if (!connections.contains(old)) {
			throw new RuntimeException("Tried to change connection that doesn't exist");
		}
		connections.remove(old);
		connections.add(newConnection);
	}
}
