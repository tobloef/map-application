package bfst19.osmdrawing.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NavigationGraph {
	List<OSMRoadNode> nodes;

	public NavigationGraph(List<OSMRoadNode> roadNodes) {
		this.nodes = roadNodes;
		//mergeConnections();
	}

	public List<OSMRoadNode> getNodes() {
		return nodes;
	}

	private void mergeConnections() {
		boolean finished = false;
		while (!finished) {
			for (int i = 0; i < nodes.size(); i++) {
				//System.out.println("Now beginning node number " + i + ": " + nodes.get(i));
				if (isTwoWayNode(nodes.get(i))) {
					OSMRoadNode origin = nodes.get(i);
					Connection leftmostConnection = origin.getConnections().get(0);
					Connection rightmostConnection = origin.getConnections().get(1);
					OSMRoadNode nextLeftmostNode = origin;
					OSMRoadNode nextRightmostNode = origin;
					OSMRoadNode leftmostNode = leftmostConnection.getOtherNode(nextLeftmostNode);
					OSMRoadNode rightmostNode = rightmostConnection.getOtherNode(nextRightmostNode);
					double summedDistance = leftmostConnection.getDistance() + rightmostConnection.getDistance();
					double summedSpeedlimit = leftmostConnection.getSpeedLimit() + rightmostConnection.getSpeedLimit();
					int connectionsToBeMerged = 2;

					//fixme man kunne måske fikse duplikering med enten en ny klasse eller statiske variabler
					while (isTwoWayNode(leftmostNode) && leftmostNode != origin) { //fixme i'm so duplicated right now
						nodes.remove(nextLeftmostNode);
						leftmostConnection = leftmostNode.getOtherConnection(leftmostConnection);
						if (leftmostConnection == null) {
							throw new RuntimeException("No node at the end of this connection");
						}
						nextLeftmostNode = leftmostNode;
						leftmostNode = leftmostConnection.getOtherNode(nextLeftmostNode);

						summedDistance += leftmostConnection.getDistance();
						summedSpeedlimit += leftmostConnection.getSpeedLimit();
						connectionsToBeMerged++;
					}
					while (rightmostNode.getConnectionAmount() == 2 && rightmostNode != origin) {
						nodes.remove(nextRightmostNode);
						rightmostConnection = rightmostNode.getOtherConnection(rightmostConnection);
						if (rightmostConnection == null) {
							throw new RuntimeException("No node at the end of this connection");
						}
						nextRightmostNode = rightmostNode;
						rightmostNode = rightmostConnection.getOtherNode(nextRightmostNode);
						
						summedDistance += rightmostConnection.getDistance();
						summedSpeedlimit += rightmostConnection.getSpeedLimit();
						connectionsToBeMerged++;
					}
					double averageSpeedLimit = summedSpeedlimit / connectionsToBeMerged;
					leftmostNode.removeConnectionToNode(nextLeftmostNode);
					rightmostNode.removeConnectionToNode(nextRightmostNode);
					Connection newConnection = new Connection(leftmostNode, rightmostNode, summedDistance, averageSpeedLimit);
					nodes.remove(origin);
					i--;
				}
			}
			finished = true;
		}
	}

	private boolean isTwoWayNode(OSMRoadNode leftmostNode) {
		return leftmostNode.getConnectionAmount() == 2;
	}
}
