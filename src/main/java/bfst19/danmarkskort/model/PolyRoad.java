package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadWay;

import java.io.Serializable;
import java.util.*;

public class PolyRoad extends Polyline implements Serializable {
	private int index;
	private double speedLimit;
	private int[] firstConnections;
	private int[] lastConnections;
	public double length = 0;
	public static PolyRoad[] allPolyRoads;
	public EnumSet<RoadRestriction> restrictions;

	public PolyRoad(OSMRoadWay way) {
		super(way);
		firstConnections = new int[0];
		lastConnections = new int[0];
		this.speedLimit = way.getSpeedLimit();
		index = -1;
		restrictions = way.getRestrictions();
		length = calculateLength();
	}

	private double calculateLength() {
		double result = 0;
		for (int i = 0; i < coords.length - 2; i += 2) {
			result += findDistanceBetweenSquared(coords[i], coords[i+1], coords[i+2], coords[i+3]);
		}
		return result;
	}

	public void addConnectionToFirst(PolyRoad road) {
		firstConnections = add(firstConnections, road.getIndex());
	}

	private int[] add(int[] connections, int index) {
		if (!contains(connections, index)){
			int[] tempArray = new int[connections.length + 1];
			for (int i = 0; i < connections.length; i++){
				tempArray[i] = connections[i];
			}
			tempArray[tempArray.length -1] = index;
			return tempArray;
		}
		else return connections;
	}

	private boolean contains(int[] connections, int index) {
		for (int i = 0; i < connections.length; i++){
			if (connections[i] == index) return true;
		}
		return false;
	}

	public void addConnectionToLast(PolyRoad road) {
		lastConnections = add(lastConnections, road.getIndex());
	}

	public double euclideanDistanceSquaredTo(PolyRoad target){
		return findDistanceBetweenSquared(getRepresentativeX(), getRepresentativeY(), target.getRepresentativeX(), target.getRepresentativeY());
	}

	public double weightedEuclideanDistanceTo(PolyRoad target){
		//130 km/t på motorveje
		return euclideanDistanceSquaredTo(target)/130;
	}

	private static double findDistanceBetweenSquared(double x1, double y1, double x2, double y2) {
		double deltaX = x1 - x2;
		double deltaY = y1 - y2;
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}

	public Set<PolyRoad> getFirstConnections() {
		Set<PolyRoad> result = new HashSet<>();
		for (Integer i : firstConnections) {
			result.add(allPolyRoads[i]);
		}
		return result;
	}

	public Set<PolyRoad> getLastConnections() {
		Set<PolyRoad> result = new HashSet<>();
		for (Integer i : lastConnections) {
			result.add(allPolyRoads[i]);
		}
		return result;
	}

	public List<PolyRoad> getAllConnections() {
		List<PolyRoad> result = new ArrayList<>();
		result.addAll(getFirstConnections());
		result.addAll(getLastConnections());
		return result;
	}

	public Set<PolyRoad> getOtherConnections(PolyRoad origin) {
		if (contains(firstConnections, origin.getIndex())) {
			return getLastConnections();
		}
		if (contains(lastConnections, origin.getIndex())) {
			return getFirstConnections();
		}
		return new HashSet<>(getAllConnections());
		//fixme the above is a hack that slows down dijkstra, we should figure out why it happens and fix it so we can use the below instead
		//throw new IllegalArgumentException("This road is not connected to specified road");
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

	public double getSpeedLimit() {
		return speedLimit;
	}

	public double getWeight() {
		return getLength() / getSpeedLimit();
	}


	public boolean wrongWay(PolyRoad origin, VehicleType vehicleType) {
		if (vehicleType != VehicleType.CAR){
			return false;
		}
		if (!isOneWay()){
			return false;
		}
		if (restrictions.contains(RoadRestriction.ONE_WAY) && contains(firstConnections, origin.getIndex())){
			return true;
		}
		else if (restrictions.contains(RoadRestriction.ONE_WAY_REVERSED) && contains(lastConnections, origin.getIndex())){
			return true;
		}
		else return false;
	}

	public boolean isOneWay() {
		return restrictions.contains(RoadRestriction.ONE_WAY) || restrictions.contains(RoadRestriction.ONE_WAY_REVERSED);
	}

	public boolean vehicleIsAllowedToTakeRoad(VehicleType vehicleType) {
		if (vehicleType == VehicleType.CAR){
			if (restrictions.contains(RoadRestriction.NO_CAR)){
				return false;
			}
		}
		else if (vehicleType == VehicleType.BICYCLE || vehicleType == VehicleType.WALKING){
			return !restrictions.contains(RoadRestriction.CAR_ONLY);
		}
		return true;
	}
}
