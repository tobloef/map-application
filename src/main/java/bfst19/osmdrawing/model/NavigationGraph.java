package bfst19.osmdrawing.model;

import java.util.List;

public class NavigationGraph {
	List<OSMRoadNode> nodes;

	public NavigationGraph(List<OSMRoadNode> roadNodes) {
		this.nodes = roadNodes;
		mergeConnections();
	}

	private void mergeConnections() {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getConnectionAmount() == 2) {
				OSMRoadNode current = nodes.get(i);
				Connection leftmostConnection = current.getConnections().get(0);
				Connection rightmostConnection = current.getConnections().get(1);
				OSMRoadNode nextLeftmostConnection = current;
				OSMRoadNode nextRightmostConnection = current;
				double summedDistance = leftmostConnection.getDistance() + rightmostConnection.getDistance();
				double summedSpeedlimit = leftmostConnection.getSpeedLimit() + rightmostConnection.getSpeedLimit();
				int connectionsToBeMerged = 2;

				while (leftmostConnection.getNode().getConnectionAmount() == 2) { //fixme i'm so duplicated right now
					nodes.remove(nextLeftmostConnection);
					nextLeftmostConnection = leftmostConnection.getNode();
					leftmostConnection = leftmostConnection.getNode().getOtherConnection(nextLeftmostConnection);
					summedDistance += leftmostConnection.getDistance();
					summedSpeedlimit += leftmostConnection.getSpeedLimit();
					connectionsToBeMerged++;
				}
				while (rightmostConnection.getNode().getConnectionAmount() == 2) { //todo right now many deleted nodes will still have references to each other, and will just float around in memory
					nodes.remove(nextRightmostConnection);
					nextRightmostConnection = rightmostConnection.getNode();
					rightmostConnection = rightmostConnection.getNode().getOtherConnection(nextRightmostConnection);
					summedDistance += rightmostConnection.getDistance();
					summedSpeedlimit += rightmostConnection.getSpeedLimit();
					connectionsToBeMerged++;
				}

				double averageSpeedLimit = summedSpeedlimit / connectionsToBeMerged;
				leftmostConnection.getNode().removeConnectionToNode(nextLeftmostConnection);
				leftmostConnection.getNode().addConnection(new Connection(rightmostConnection.getNode(), summedDistance, averageSpeedLimit));
				rightmostConnection.getNode().removeConnectionToNode(nextRightmostConnection);
				rightmostConnection.getNode().addConnection(new Connection(leftmostConnection.getNode(), summedDistance, averageSpeedLimit));
			}
		}
	}
}
