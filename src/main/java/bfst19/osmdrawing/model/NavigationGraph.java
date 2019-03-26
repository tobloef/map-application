package bfst19.osmdrawing.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NavigationGraph {
	List<NavigationNode> nodes;

	public NavigationGraph() {
		this.nodes = new ArrayList<>();

	}

	public void addNode(OSMNode node) {
	}

	private class NavigationNode {
		Point2D coordinate;
		Map<NavigationNode, Double> connectedNodes;

		public NavigationNode(double x, double y) {
			coordinate = new Point2D(x, y);
		}

		public void addConnection(NavigationNode node, double distance) {
			connectedNodes.put(node, distance);
		}


	}
}
