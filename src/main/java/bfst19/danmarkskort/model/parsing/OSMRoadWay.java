package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.RoadRestriction;
import bfst19.danmarkskort.model.WayType;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class OSMRoadWay extends OSMWay {
	private int speedLimit;
	private WayType type;
	private EnumSet<RoadRestriction> restrictions;

	//used for making new road based on OSMWay
	public OSMRoadWay(OSMWay way, int speedLimit, WayType type, EnumSet<RoadRestriction> restrictions) {
		this(way, way.getNodes(), speedLimit, type, restrictions);
	}

	//used for splitting existing road
	public OSMRoadWay(OSMWay way, List<? extends OSMNode> newNodes, int speedLimit, WayType type, EnumSet<RoadRestriction> restrictions) {
		super(way.id);
		list = new ArrayList<>();
		for (OSMNode node : newNodes) {
			OSMRoadNode actual = (OSMRoadNode) node;
			actual.add(this);
		}
		list.addAll(newNodes);
		this.speedLimit = speedLimit;
		this.type = type;
		this.restrictions = restrictions;
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
				OSMRoadWay result = new OSMRoadWay(new OSMWay(id, splitNodes), speedLimit, type, restrictions);
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

	public EnumSet<RoadRestriction> getRestrictions() {
		return restrictions;
	}

	public void addRestriction(RoadRestriction restriction) {
		restrictions.add(restriction);
	}
}
