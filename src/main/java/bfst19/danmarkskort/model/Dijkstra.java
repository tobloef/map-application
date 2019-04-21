package bfst19.danmarkskort.model;

import java.util.*;

public class Dijkstra {
	public static Set<PolyRoad> lastUsedRoads = new HashSet<>();
	public static HashMap<PolyRoad, PolyRoad> previousRoads;

	public static List<PolyRoad> getShortestPath(PolyRoad origin, PolyRoad destination, VehicleType vehicleType) throws DisconnectedRoadsException {
		double[] distTo = initializeDistTo(origin);
		previousRoads = new HashMap<>();
		IndexMinPQ<Double> remainingPolyRoads = new IndexMinPQ<>(PolyRoad.allPolyRoads.length);

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

			for(PolyRoad connectedRoad : connections){
				if (current.wrongWay(connectedRoad, vehicleType)) {
					continue;
				}
				if (!connectedRoad.vehicleIsAllowedToTakeRoad(vehicleType)){
					continue;
				}
				int thisConnectionIndex = connectedRoad.getIndex();
				double connectedRoadWeight = calculateWeight(connectedRoad, destination, vehicleType);
				if(distTo[thisConnectionIndex] > distTo[current.getIndex()] + connectedRoadWeight){
					distTo[thisConnectionIndex] = distTo[current.getIndex()] + connectedRoadWeight;
					previousRoads.put(connectedRoad, current);
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

	private static void insertStartConnections(PolyRoad origin, PolyRoad destination,  IndexMinPQ<Double> remainingPolyRoads, VehicleType vehicleType) {
		for (PolyRoad connectedRoad : origin.getAllConnections()){
			previousRoads.put(connectedRoad, origin);
			remainingPolyRoads.insert(connectedRoad.getIndex(), calculateWeight(connectedRoad, destination, vehicleType));
		}
	}

	private static double calculateWeight(PolyRoad polyRoad, PolyRoad destination, VehicleType vehicleType) {
		double weight;
		if (vehicleType == VehicleType.CAR){
			weight = polyRoad.getWeight();
		}
		else {
			weight = polyRoad.getLength();
		}
		return weight;
	}

	private static double[] initializeDistTo(PolyRoad origin) {
		double[] distTo = new double[PolyRoad.allPolyRoads.length];
		for(int i = 0; i < distTo.length; i++){
			distTo[i] = Double.POSITIVE_INFINITY;
		}
		distTo[origin.getIndex()] = 0;
		return distTo;
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
