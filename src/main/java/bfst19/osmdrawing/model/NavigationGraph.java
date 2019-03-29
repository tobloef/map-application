package bfst19.osmdrawing.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NavigationGraph {
	List<OSMRoadNode> nodes;

	public NavigationGraph(List<OSMRoadNode> roadNodes) {
		this.nodes = roadNodes;
		mergeConnections();
	}

	public List<OSMRoadNode> getNodes() {
		return nodes;
	}

	private void mergeConnections() {
		boolean finished = false;
		while (!finished) {
			for (int i = 0; i < nodes.size(); i++) {
				//System.out.println("Now beginning node number " + i + ": " + nodes.get(i));
				if (nodes.get(i).getConnectionAmount() == 2) {
					OSMRoadNode origin = nodes.get(i);
					Connection leftmostConnection = origin.getConnections().get(0);
					Connection rightmostConnection = origin.getConnections().get(1);
					OSMRoadNode nextLeftmostConnection = origin;
					OSMRoadNode nextRightmostConnection = origin;
					double summedDistance = leftmostConnection.getDistance() + rightmostConnection.getDistance();
					double summedSpeedlimit = leftmostConnection.getSpeedLimit() + rightmostConnection.getSpeedLimit();
					int connectionsToBeMerged = 2;

					while (leftmostConnection.getNode().getConnectionAmount() == 2 && leftmostConnection.getNode() != origin) { //fixme i'm so duplicated right now
						nodes.remove(nextLeftmostConnection);
						OSMRoadNode temp = leftmostConnection.getNode();
						leftmostConnection = leftmostConnection.getNode().getOtherConnection(nextLeftmostConnection);
						if (leftmostConnection == null) {
							System.out.println("help");
						}
						nextLeftmostConnection = temp;
						summedDistance += leftmostConnection.getDistance();
						summedSpeedlimit += leftmostConnection.getSpeedLimit();
						connectionsToBeMerged++;
					}
					while (rightmostConnection.getNode().getConnectionAmount() == 2 && rightmostConnection.getNode() != origin) { //todo right now many deleted nodes will still have references to each other, and will just float around in memory
						nodes.remove(nextRightmostConnection);
						OSMRoadNode temp = rightmostConnection.getNode();
						rightmostConnection = rightmostConnection.getNode().getOtherConnection(nextRightmostConnection);
						nextRightmostConnection = temp;
						summedDistance += rightmostConnection.getDistance();
						summedSpeedlimit += rightmostConnection.getSpeedLimit();
						connectionsToBeMerged++;
					}
					double averageSpeedLimit = summedSpeedlimit / connectionsToBeMerged;
					leftmostConnection.getNode().removeConnectionToNode(nextLeftmostConnection);
					leftmostConnection.getNode().addConnection(new Connection(rightmostConnection.getNode(), summedDistance, averageSpeedLimit));
					rightmostConnection.getNode().removeConnectionToNode(nextRightmostConnection);
					rightmostConnection.getNode().addConnection(new Connection(leftmostConnection.getNode(), summedDistance, averageSpeedLimit));
					nodes.remove(origin);
					i--;
				}
			}
			finished = true;
		}
	}
}
