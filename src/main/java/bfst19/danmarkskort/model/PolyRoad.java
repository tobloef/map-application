package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRoadWay;

import java.io.Serializable;
import java.util.*;

public class PolyRoad extends Polyline implements Serializable {
	private int index;
	private double speedLimit;
	private String streetName;
	private int[] firstConnections;
	private int[] lastConnections;
	private double length = 0;
	private static PolyRoad[] allPolyRoads;
	private EnumSet<RoadRestriction> restrictions;

	public static PolyRoad getPolyRoadFromIndex(int index){
		return allPolyRoads[index];
	}

	public PolyRoad(OSMRoadWay way) {
		super(way);
		firstConnections = new int[0];
		lastConnections = new int[0];
		streetName = way.getStreetName();
		speedLimit = way.getSpeedLimit();
		index = -1;
		restrictions = way.getRestrictions();
		length = calculateLength();
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

	public void removeDuplicateConnections(){
		this.firstConnections = getUniqueValues(this.firstConnections);
		this.lastConnections = getUniqueValues(this.lastConnections);
	}

	private static int[] getUniqueValues(int arr[]){
		int n = arr.length;
		if (n==0 || n==1){
			return arr;
		}
		//Sort it and ignore duplicates
		Arrays.sort(arr);
		int[] temp = new int[n];
		int j = 0;
		for (int i=0; i<n-1; i++){
			if (arr[i] != arr[i+1]){
				temp[j++] = arr[i];
			}
		}
		temp[j++] = arr[n-1];
		//Size it correctly.
		int[] tempToSize = new int[j];
		for (int i = 0; i < j; i++){
			tempToSize[i] = temp[i];
		}
		return tempToSize;
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
		return deltaX*deltaX + deltaY*deltaY;
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


	public int[] getAllConnections(){
		int[] result = new int[firstConnections.length + lastConnections.length];
		int i = 0;
		for (; i < firstConnections.length; i++){
			result[i] = firstConnections[i];
		}
		for (int j = 0; j < lastConnections.length; i++, j++){
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
