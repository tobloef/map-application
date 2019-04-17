package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.WayType;

import java.util.ArrayList;
import java.util.List;

public class OSMRoadWay extends OSMWay {
	private double speedLimit;
	private WayType type;

	public OSMRoadWay(OSMWay oldWay, double speedLimit, WayType type) {
		super(oldWay.id);
		list = oldWay.getNodes();
		for (OSMRoadNode node : getNodesAsRoadNodes()) {
			node.add(this);
		}
		this.speedLimit = speedLimit;
		this.type = type;

	}

	public OSMRoadWay(OSMWay way, List<OSMRoadNode> newNodes, int speedLimit, WayType type) {
		super(way.id);
		list = new ArrayList<>();
		for (OSMRoadNode node : newNodes) {
			node.add(this);
		}
		list.addAll(newNodes);
		this.speedLimit = speedLimit;
		this.type = type;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public boolean hasValidNodes() {
		for (OSMNode node : list) {
			OSMRoadNode roadNode = (OSMRoadNode) node;
			if (!roadNode.getConnections().contains(this)) {
				return false;
			}
		}
		return true;
	}

	public OSMRoadWay splitIfNeeded() {
		for (int i = 1; i < list.size() - 1; i++) {

			OSMRoadNode node;
			if (list.get(i) instanceof OSMRoadNode) {
				node = (OSMRoadNode) list.get(i);
			}
			else {
				throw new RuntimeException("Nodes in OSMRoadWay must be OSMRoadNode, not OSMNode");
			}
			if (node.getConnectionAmount() > 1) {
				List<OSMNode> splitNodes = list.subList(i, list.size());
				list = list.subList(0, i+1); //this uses i+1 since both lists needs to have the connecting node
				OSMRoadWay result = new OSMRoadWay(new OSMWay(id, splitNodes), speedLimit, type);
				for (OSMNode n : splitNodes) {
					if (n == splitNodes.get(0)) {
						continue;
					}
					OSMRoadNode casted = (OSMRoadNode) n;
					casted.removeConnection(this);
				}
				if (result.size() <= 0) {
					throw new RuntimeException("Created empty way");
				}
				return result;
			}
		}
		return null;
	}

	public List<OSMRoadNode> getNodesAsRoadNodes() {
		List<OSMRoadNode> result = new ArrayList<>();
		for (OSMNode node : list) {
			result.add((OSMRoadNode) node);
		}
		return result;
	}

	public WayType getType() {
		return type;
	}
}
