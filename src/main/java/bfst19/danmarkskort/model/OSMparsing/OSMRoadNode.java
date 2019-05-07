package bfst19.danmarkskort.model.OSMparsing;

import java.util.ArrayList;
import java.util.List;

public class OSMRoadNode extends OSMNode {
    private final List<OSMRoadWay> connections;

    public OSMRoadNode(OSMNode node) {
        super(node.getAsLong(), node.getLon(), node.getLat());
        connections = new ArrayList<>();
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

    public List<OSMRoadWay> getConnections() {
        return connections;
    }

    public String toString() {
        return "" + getAsLong();
    }

    public void removeConnection(OSMRoadWay removal) {
        connections.remove(removal);
    }
}
