package bfst19.danmarkskort.model;

import java.util.*;

public class Dijkstra {

	public static List<PolyRoad> getShortestPath(PolyRoad origin, PolyRoad destination){
		double[] distTo = new double[PolyRoad.allPolyRoads.length];
		HashMap<PolyRoad, PolyRoad> previousRoads = new HashMap<>();
		IndexMinPQ<Double> remainingPolyRoads = new IndexMinPQ<>(PolyRoad.allPolyRoads.length);

		for(int i = 0; i < distTo.length; i++){
			distTo[i] = Double.POSITIVE_INFINITY;
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
				int thisConnectionIndex = thisConnection.getIndex();
				if(distTo[thisConnectionIndex] > distTo[current.getIndex()] + current.getLength()){
					distTo[thisConnectionIndex] = distTo[current.getIndex()] + current.getLength();
					previousRoads.put(thisConnection, current);
					if(remainingPolyRoads.contains(thisConnectionIndex)){
						remainingPolyRoads.changeKey(thisConnectionIndex, distTo[thisConnectionIndex]);
					}
					else{
						remainingPolyRoads.insert(thisConnectionIndex, distTo[thisConnectionIndex]);
					}
				}
				if(PolyRoad.allPolyRoads[thisConnectionIndex] == destination){
					List<PolyRoad> path = new ArrayList<>();
					path.add(destination);
					while(path.get(path.size()-1) != origin){
						path.add(previousRoads.get(path.get(path.size()-1)));
					}
					Collections.reverse(path);
					return path;
				}
			}
		}
		throw new IllegalArgumentException("There is no connection between the two roads");
	}
}
