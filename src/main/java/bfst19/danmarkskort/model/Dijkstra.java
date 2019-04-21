package bfst19.danmarkskort.model;

import java.util.*;

public class Dijkstra {
	public static HashMap<PolyRoad, PolyRoad> pathToRoad;
	private static double[] distTo;

	public static List<PolyRoad> getShortestPath(PolyRoad origin, PolyRoad destination, VehicleType vehicleType) throws DisconnectedRoadsException {
		distTo = initializeDistTo(origin);
		pathToRoad = new HashMap<>();
		IndexMinPQ<Double> remainingPolyRoads = new IndexMinPQ<>(PolyRoad.allPolyRoads.length);

		insertStartConnections(origin,destination, remainingPolyRoads, vehicleType);


		while(!remainingPolyRoads.isEmpty()){
			PolyRoad current = PolyRoad.allPolyRoads[remainingPolyRoads.delMin()];
			for(PolyRoad connectedRoad : current.getAllConnections()){
				if (pathToRoad.containsKey(connectedRoad)){
					continue;
				}
				if (notAllowedToTakeRoad(vehicleType, current, connectedRoad)) {
					continue;
				}
				int connectedRoadIndex = connectedRoad.getIndex();
				double connectedRoadWeight = calculateWeight(connectedRoad, destination, vehicleType);
				if(distTo[connectedRoadIndex] > distTo[current.getIndex()] + connectedRoadWeight){
					distTo[connectedRoadIndex] = distTo[current.getIndex()] + connectedRoadWeight;
					pathToRoad.put(connectedRoad, current);
					if(remainingPolyRoads.contains(connectedRoadIndex)){
						remainingPolyRoads.changeKey(connectedRoadIndex, distTo[connectedRoadIndex]);
					}
					else{
						remainingPolyRoads.insert(connectedRoadIndex, distTo[connectedRoadIndex]);
					}
				}
			}
			if(foundPathTo(destination)){
				List<PolyRoad> route = makeRoute(origin, destination, pathToRoad);
				System.out.println(routeLength(route));
				return route;
			}
		}
		throw new DisconnectedRoadsException("There is no connection between the two roads", origin, destination);
	}

	private static boolean foundPathTo(PolyRoad destination) {
		return pathToRoad.get(destination) != null;
	}

	private static boolean notAllowedToTakeRoad(VehicleType vehicleType, PolyRoad current, PolyRoad connectedRoad) {
		if (current.wrongWay(connectedRoad, vehicleType)) {
			return true;
		}
		if (!connectedRoad.vehicleIsAllowedToTakeRoad(vehicleType)){
			return true;
		}
		return false;
	}

	private static void insertStartConnections(PolyRoad origin, PolyRoad destination,  IndexMinPQ<Double> remainingPolyRoads, VehicleType vehicleType) {
		for (PolyRoad connectedRoad : origin.getAllConnections()){
			pathToRoad.put(connectedRoad, origin);
			double weightToConnectedRoad = calculateWeight(connectedRoad, destination, vehicleType);
			distTo[connectedRoad.getIndex()] = weightToConnectedRoad;
			remainingPolyRoads.insert(connectedRoad.getIndex(), weightToConnectedRoad);
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

	public static Iterable<? extends PolyRoad> getLastVisitedRoads() {
		return pathToRoad.keySet();
	}
}
