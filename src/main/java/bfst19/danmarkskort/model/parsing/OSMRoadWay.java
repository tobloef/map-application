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
					OSMRoadNode casted = (OSMRoadNode) n;
					casted.changeConnection(this, result);
				}
				if (result.size() <= 0) {
					throw new RuntimeException("Created empty way");
				}
				return result;
			}
		}
		return null;
	}

	public WayType getType() {
		return type;
	}
}
