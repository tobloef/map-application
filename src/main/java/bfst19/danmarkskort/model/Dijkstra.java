package bfst19.danmarkskort.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {

	public List<PolyRoad> getShortestPath(PolyRoad origin, PolyRoad destination){
		double[] distTo = new double[PolyRoad.allPolyRoads.length];
		HashMap<PolyRoad, PolyRoad> previousRoads = new HashMap<>();
		PriorityQueue<RoadWithDistance> remainingPolyRoads = new PriorityQueue<>();

		for(double distance : distTo){
			distance = Double.POSITIVE_INFINITY;
		}

		distTo[origin.getIndex()] = 0;
		remainingPolyRoads.add(new RoadWithDistance(origin, 0.0));
		while(!remainingPolyRoads.isEmpty()){
			RoadWithDistance current = remainingPolyRoads.poll();
			List<PolyRoad> connections = new ArrayList<>();

			if(current.polyRoad == origin){
				connections.addAll(current.polyRoad.getFirstConnections());
				connections.addAll(current.polyRoad.getLastConnections());
			}
			else{
				connections.addAll(current.polyRoad.getOtherConnections(current.polyRoad));
			}
			for(PolyRoad thisConnection : connections){
				if(distTo[thisConnection.getIndex()] > distTo[current.polyRoad.getIndex()] + current.distance){
					distTo[thisConnection.getIndex()] = distTo[current.polyRoad.getIndex()] + current.distance;
					previousRoads.put(thisConnection, current.polyRoad);
				}
			}
		}

	}

	private class RoadWithDistance implements Comparable<RoadWithDistance>{
		public PolyRoad polyRoad;
		public double distance;

		public RoadWithDistance(PolyRoad polyRoad, double distance) {
			this.polyRoad = polyRoad;
			this.distance = distance;
		}

		@Override
		public int compareTo(RoadWithDistance o) {
			if(distance < o.distance){
				return -1;
			}
			if(distance == o.distance){
				return 0;
			}
			return 1;
		}
	}
}
