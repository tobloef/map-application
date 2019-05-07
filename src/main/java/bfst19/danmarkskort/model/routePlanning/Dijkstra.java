package bfst19.danmarkskort.model.routePlanning;

import bfst19.danmarkskort.exceptions.DisconnectedRoadsException;
import bfst19.danmarkskort.model.drawables.PolyRoad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dijkstra {
    public static int[] pathToRoad;
    private static double[] distTo;

    public static Route getShortestPath(PolyRoad origin, PolyRoad destination, VehicleType vehicleType) throws DisconnectedRoadsException {
        distTo = initializeDistTo(origin);
        pathToRoad = new int[PolyRoad.getNumberOfRoads()];
        IndexMinPQ<Double> remainingPolyRoads = new IndexMinPQ<>(PolyRoad.getNumberOfRoads());

        insertStartConnections(origin, destination, remainingPolyRoads, vehicleType);


        while (!remainingPolyRoads.isEmpty()) {
            PolyRoad current = PolyRoad.getPolyRoadFromIndex(remainingPolyRoads.delMin());
            for (int connectedRoadIndex : current.getAllConnections()) {
                PolyRoad connectedRoad = PolyRoad.getPolyRoadFromIndex(connectedRoadIndex);
                if (pathToRoad[connectedRoadIndex] != 0) {
                    continue;
                }
                if (notAllowedToTakeRoad(vehicleType, current, connectedRoad)) {
                    continue;
                }
                double connectedRoadWeight = calculateWeight(connectedRoad, destination, vehicleType);
                if (distTo[connectedRoadIndex] > distTo[current.getIndex()] + connectedRoadWeight) {
                    distTo[connectedRoadIndex] = distTo[current.getIndex()] + connectedRoadWeight;
                    pathToRoad[connectedRoadIndex] = current.getIndex();
                    if (remainingPolyRoads.contains(connectedRoadIndex)) {
                        remainingPolyRoads.changeKey(connectedRoadIndex, distTo[connectedRoadIndex]);
                    } else {
                        remainingPolyRoads.insert(connectedRoadIndex, distTo[connectedRoadIndex]);
                    }
                }
            }
            if (foundPathTo(destination)) {
                Route route = makeRoute(origin, destination, pathToRoad);
                cleanup();
                return route;
            }
        }
        cleanup();
        throw new DisconnectedRoadsException("There is no connection between the two roads", origin, destination);
    }

    private static void cleanup() {
        distTo = null;
    }

    private static boolean foundPathTo(PolyRoad destination) {
        return pathToRoad[destination.getIndex()] != 0;
    }

    private static boolean notAllowedToTakeRoad(VehicleType vehicleType, PolyRoad current, PolyRoad connectedRoad) {
        if (current.wrongWay(connectedRoad, vehicleType)) {
            return true;
        }
		return !connectedRoad.vehicleIsAllowedToTakeRoad(vehicleType);
	}

    private static void insertStartConnections(PolyRoad origin, PolyRoad destination, IndexMinPQ<Double> remainingPolyRoads, VehicleType vehicleType) {
        for (int connectedRoadIndex : origin.getAllConnections()) {
            PolyRoad connectedRoad = PolyRoad.getPolyRoadFromIndex(connectedRoadIndex);
            if (remainingPolyRoads.contains(connectedRoad.getIndex())) {
                continue;
            }
            pathToRoad[connectedRoad.getIndex()] = origin.getIndex();
            double weightToConnectedRoad = calculateWeight(connectedRoad, destination, vehicleType);
            distTo[connectedRoad.getIndex()] = weightToConnectedRoad;
            remainingPolyRoads.insert(connectedRoad.getIndex(), weightToConnectedRoad);
        }
    }

    private static double calculateWeight(PolyRoad polyRoad, PolyRoad destination, VehicleType vehicleType) {
        double weight;
        if (vehicleType == VehicleType.CAR) {
            weight = polyRoad.getWeight();
        } else {
            weight = polyRoad.getLength();
        }
        weight += calculateHeuristics(polyRoad, destination, vehicleType);
        return weight;
    }

    private static double calculateHeuristics(PolyRoad polyRoad, PolyRoad destination, VehicleType vehicleType) {
        double weight;
        if (vehicleType == VehicleType.CAR) {
            weight = polyRoad.weightedEuclideanDistanceSquaredTo(destination);
        } else {
            weight = polyRoad.euclideanDistanceSquaredToSqaured(destination);
        }
        return weight;
    }

    private static double[] initializeDistTo(PolyRoad origin) {
        double[] distTo = new double[PolyRoad.getNumberOfRoads()];
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
        distTo[origin.getIndex()] = 0;
        return distTo;
    }

    private static Route makeRoute(PolyRoad origin, PolyRoad destination, int[] previousRoads) {
        Route path = new Route();
        path.add(destination);
        while (path.get(path.size() - 1) != origin) {
            int prevRoadIndex = previousRoads[(path.get(path.size() - 1)).getIndex()];
            PolyRoad prevRoad = PolyRoad.getPolyRoadFromIndex(prevRoadIndex);
            path.add(prevRoad);
        }
        Collections.reverse(path);
        return path;
    }

    public static Iterable<? extends PolyRoad> getLastVisitedRoads() {
        if (pathToRoad != null) {
            List<PolyRoad> roads = new ArrayList<>();
            for (int roadIndex : pathToRoad) {
                if (roadIndex == 0) {
                    continue;
                }
                roads.add(PolyRoad.getPolyRoadFromIndex(roadIndex));
            }
            return roads;
        } else return new ArrayList<>();
    }
}