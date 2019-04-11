package bfst19.danmarkskort.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {

	public List<PolyRoad> getShortestPath(PolyRoad origin, PolyRoad destination){
		double[] distTo = new double[PolyRoad.allPolyRoads.length];
		HashMap<PolyRoad, PolyRoad> previousRoads = new HashMap<>();
		IndexMinPQ<Double> remainingPolyRoads = new IndexMinPQ<>(PolyRoad.allPolyRoads.length);

		for(double distance : distTo){
			distance = Double.POSITIVE_INFINITY;
		}

		distTo[origin.getIndex()] = 0;
		remainingPolyRoads.insert(origin.getIndex(), 0.0);
		while(!remainingPolyRoads.isEmpty()){
			PolyRoad current = PolyRoad.allPolyRoads[remainingPolyRoads.delMin()];
			List<PolyRoad> connections = new ArrayList<>();

			if(current == origin){
				connections.addAll(current.getFirstConnections());
				connections.addAll(current.getLastConnections());
			}
			else{
				connections.addAll(current.getOtherConnections(current));
			}
			for(PolyRoad thisConnection : connections){
				int w = thisConnection.getIndex();
				if(distTo[w] > distTo[current.getIndex()] + current.getLength()){
					distTo[w] = distTo[current.getIndex()] + current.getLength();
					previousRoads.put(thisConnection, current);
					if(remainingPolyRoads.contains(w)){
						remainingPolyRoads.changeKey(w, distTo[w]);
					}
					else{
						remainingPolyRoads.insert(w, distTo[w]);
					}
				}
				if(PolyRoad.allPolyRoads[w] == destination){
					List<PolyRoad> path = new ArrayList<>();
					while(path.get(path.size()-1) != origin){
						path.add(previousRoads.get(path.get(path.size()-1)));
					}
					return path;
				}
			}
		}
		throw new IllegalArgumentException("There is no connection between the two roads");
	}
}
