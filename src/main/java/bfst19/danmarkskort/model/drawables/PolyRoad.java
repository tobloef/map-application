package bfst19.danmarkskort.model.drawables;

import bfst19.danmarkskort.model.OSMparsing.OSMRoadWay;
import bfst19.danmarkskort.model.routePlanning.RoadRestriction;
import bfst19.danmarkskort.model.routePlanning.VehicleType;
import bfst19.danmarkskort.utils.Misc;

import java.io.Serializable;
import java.util.*;

public class PolyRoad extends Polyline implements Serializable {
    private final static String defaultStreetName = "unknown road";
    private static PolyRoad[] allPolyRoads;
    private int index;
    private final double speedLimit;
    private final String streetName;
    private int[] firstConnections;
    private int[] lastConnections;
    private double length;
    private final EnumSet<RoadRestriction> restrictions;

    public PolyRoad(OSMRoadWay way) {
        super(way);
        firstConnections = new int[0];
        lastConnections = new int[0];
        streetName = way.getStreetName();
        speedLimit = way.getSpeedLimit();
        index = -1;
        restrictions = way.getRestrictions();
        length = calculateLengthSquared();
    }

    public static PolyRoad getPolyRoadFromIndex(int index) {
        return allPolyRoads[index];
    }

    public static int getNumberOfRoads() {
        return allPolyRoads.length;
    }

    public static void setPolyRoadByIndex(int i, PolyRoad road) {
        allPolyRoads[i] = road;
    }

    public static void initializePolyRoadRegister(int n) {
        allPolyRoads = new PolyRoad[n];
    }

    public static PolyRoad[] getAllPolyRoads() {
        return allPolyRoads;
    }

    public static void setAllPolyRoads(PolyRoad[] newAllPolyRoads) {
        allPolyRoads = newAllPolyRoads;
    }

    private static int[] getUniqueValues(int[] arr) { //todo fix names
        int n = arr.length;
        if (n == 0 || n == 1) {
            return arr;
        }
        //Sort it and ignore duplicates
        Arrays.sort(arr);
        int[] temp = new int[n];
        int j = 0;
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] != arr[i + 1]) {
                temp[j++] = arr[i];
            }
        }
        temp[j++] = arr[n - 1];
        //Size it correctly.
        int[] tempToSize = new int[j];
		System.arraycopy(temp, 0, tempToSize, 0, j);
        return tempToSize;
    }

    private static double findDistanceBetweenSquared(double x1, double y1, double x2, double y2) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return deltaX * deltaX + deltaY * deltaY;
    }

    public void removeDuplicateConnections() {
        this.firstConnections = getUniqueValues(this.firstConnections);
        this.lastConnections = getUniqueValues(this.lastConnections);
    }

    private double calculateLengthSquared() {
        double result = 0;
        for (int i = 0; i < coords.length - 2; i += 2) {
            result += findDistanceBetweenSquared(coords[i], coords[i + 1], coords[i + 2], coords[i + 3]);
        }
        return result;
    }

    public void addConnectionToFirst(PolyRoad road) {
        firstConnections = add(firstConnections, road.getIndex());
    }

    private int[] add(int[] connections, int index) {
        if (!contains(connections, index)) {
            int[] tempArray = new int[connections.length + 1];
			Misc.arrayCopy(connections, tempArray);
			tempArray[tempArray.length - 1] = index;
            return tempArray;
        } else return connections;
    }

	private boolean contains(int[] connections, int index) {
		for (int connection : connections) {
			if (connection == index) return true;
		}
        return false;
    }

    public void addConnectionToLast(PolyRoad road) {
        lastConnections = add(lastConnections, road.getIndex());
    }

    public double euclideanDistanceSquaredToSqaured(PolyRoad target) {
        return findDistanceBetweenSquared(getRepresentativeX(), getRepresentativeY(), target.getRepresentativeX(), target.getRepresentativeY());
    }

    public double weightedEuclideanDistanceSquaredTo(PolyRoad target) {
        //130 km/t på motorveje
        return euclideanDistanceSquaredToSqaured(target) / 130;
    }

    public Collection<PolyRoad> getFirstConnections() {
        List<PolyRoad> result = new ArrayList<>();
        for (Integer i : firstConnections) {
            result.add(allPolyRoads[i]);
        }
        return result;
    }

    public Collection<PolyRoad> getLastConnections() {
        List<PolyRoad> result = new ArrayList<>();
        for (Integer i : lastConnections) {
            result.add(allPolyRoads[i]);
        }
        return result;
    }


    public int[] getAllConnections() {
        int[] result = new int[firstConnections.length + lastConnections.length];
        int i = 0;
        for (; i < firstConnections.length; i++) {
            result[i] = firstConnections[i];
        }
        for (int j = 0; j < lastConnections.length; i++, j++) {
            result[i] = lastConnections[j];
        }
        return result;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "" + index;
    }

    public double getLength() {
        return length;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNameOrDefault() {
        if (streetName == null) {
            return defaultStreetName;
        }
        return streetName;
    }

    public double getRealLength() {
        double length = 0;
        for (int i = 0; i < coords.length - 2; i += 2) {
            length += findDistanceBetween(coords[i], coords[i + 1], coords[i + 2], coords[i + 3]);
        }
        return length;
    }

    private double findDistanceBetween(float x1, float y1, float x2, float y2) {
        return Math.sqrt(findDistanceBetweenSquared(x1, y1, x2, y2));
    }

    public double getDurationInMinutes() {
        return (getRealLength() * 110 / getSpeedLimit()) * 60;
    }

    public double getDegree(PolyRoad connectedRoad) {
        int end = coords.length;
        double deltaX;
        double deltaY;
        if (getFirstConnections().contains(connectedRoad)) {
            deltaX = coords[0] - coords[2];
            deltaY = coords[1] - coords[3];
        } else if (getLastConnections().contains(connectedRoad)) {
            deltaX = coords[end - 2] - coords[end - 4];
            deltaY = coords[end - 1] - coords[end - 3];
        } else {
            throw new RuntimeException("Roads are not connected" + Arrays.toString(connectedRoad.getAllConnections()));
        }
        return Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public double getWeight() {
        return getLength() / getSpeedLimit();
    }


    public boolean wrongWay(PolyRoad origin, VehicleType vehicleType) {
        if (vehicleType != VehicleType.CAR) {
            return false;
        }
        if (!isOneWay()) {
            return false;
        }
        if (restrictions.contains(RoadRestriction.ONE_WAY) && contains(firstConnections, origin.getIndex())) {
            return true;
        } else
            return restrictions.contains(RoadRestriction.ONE_WAY_REVERSED) && contains(lastConnections, origin.getIndex());
    }

    public boolean isOneWay() {
        return restrictions.contains(RoadRestriction.ONE_WAY) || restrictions.contains(RoadRestriction.ONE_WAY_REVERSED);
    }

    public boolean vehicleIsAllowedToTakeRoad(VehicleType vehicleType) {
        if (vehicleType == VehicleType.CAR) {
			return !restrictions.contains(RoadRestriction.NO_CAR);
        } else if (vehicleType == VehicleType.BICYCLE || vehicleType == VehicleType.WALKING) {
            return !restrictions.contains(RoadRestriction.CAR_ONLY);
        }
        return true;
    }
}
