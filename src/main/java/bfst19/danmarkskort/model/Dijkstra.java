package bfst19.danmarkskort.model;

import java.util.*;

public class Dijkstra {
	public static Set<PolyRoad> lastUsedRoads = new HashSet<>();

	public static List<PolyRoad> getShortestPath(PolyRoad origin, PolyRoad destination) throws DisconnectedRoadsException {
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
			double heuristicTo = current.euclideanDistanceSquaredTo(destination);
			List<PolyRoad> connections = new ArrayList<>();

			if(current == origin){

				connections.addAll(current.getAllConnections());
			}
			else{

				connections.addAll(current.getOtherConnections(previousRoads.get(current)));
			}

			for(PolyRoad thisConnection : connections){
				int thisConnectionIndex = thisConnection.getIndex();
				if (thisConnection.wrongWay(current)) {
					continue;
				}
				if(distTo[thisConnectionIndex] > distTo[current.getIndex()] + current.getWeight()){
					distTo[thisConnectionIndex] = distTo[current.getIndex()] + current.getWeight();
					previousRoads.put(thisConnection, current);
					if(remainingPolyRoads.contains(thisConnectionIndex)){
						remainingPolyRoads.changeKey(thisConnectionIndex, distTo[thisConnectionIndex]);
					}
					else{
						remainingPolyRoads.insert(thisConnectionIndex, distTo[thisConnectionIndex]);
					}
				}
			}
			if(previousRoads.get(destination) != null){
				List<PolyRoad> route = makeRoute(origin, destination, previousRoads);
				System.out.println(routeLength(route));
				Set<PolyRoad> usedRoads = new HashSet<>();
				for (PolyRoad road : previousRoads.keySet()) {
					if (previousRoads.get(road) != null) {
						usedRoads.add(road);
					}
				}
				lastUsedRoads = usedRoads;
				return route;
			}
		}
		throw new DisconnectedRoadsException("There is no connection between the two roads", origin, destination);
	}

	private static List<PolyRoad> makeRoute(PolyRoad origin, PolyRoad destination, HashMap<PolyRoad, PolyRoad> previousRoads) {
		List<PolyRoad> path = new ArrayList<>();
		path.add(destination);
		while(path.get(path.size()-1) != origin){
			path.add(previousRoads.get(path.get(path.size()-1)));
		}
		Collections.reverse(path);
		return path;
	}

	public static double routeLength(List<PolyRoad> route) {
		return route.stream().mapToDouble(PolyRoad::getLength).sum();
	}
}
