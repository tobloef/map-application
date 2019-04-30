package bfst19.danmarkskort.model;

import java.util.*;

public class RoadInformation {

	public static Map<String, Integer> speedLimitsFromTags = initSpeedLimitsFromTags();
	public static Set<WayType> roadTypes = getRoadTypes();
	public static Map<VehicleType, EnumSet<WayType>> allowedRoadTypes = initAllowedRoadTypes();

	private static Map<VehicleType, EnumSet<WayType>> initAllowedRoadTypes() {
		Map<VehicleType, EnumSet<WayType>> allowedRoadTypes = new EnumMap<>(VehicleType.class);
		for (VehicleType vehicleType : VehicleType.values()){
			allowedRoadTypes.put(vehicleType, EnumSet.noneOf(WayType.class));
		}
		allowedRoadTypes.get(VehicleType.CAR).add(WayType.RESIDENTIAL_ROAD);
		allowedRoadTypes.get(VehicleType.CAR).add(WayType.SERVICE_ROAD);
		allowedRoadTypes.get(VehicleType.CAR).add(WayType.TERTIARY_ROAD);
		allowedRoadTypes.get(VehicleType.CAR).add(WayType.LIVING_STREET);
		allowedRoadTypes.get(VehicleType.CAR).add(WayType.SECONDARY_ROAD);
		allowedRoadTypes.get(VehicleType.CAR).add(WayType.UNCLASSIFIED_ROAD);
		allowedRoadTypes.get(VehicleType.CAR).add(WayType.PRIMARY_ROAD);
		allowedRoadTypes.get(VehicleType.CAR).add(WayType.MOTORWAY);
		allowedRoadTypes.get(VehicleType.WALKING).addAll(getRoadTypes());
		allowedRoadTypes.get(VehicleType.WALKING).remove(WayType.MOTORWAY);
		allowedRoadTypes.get(VehicleType.BICYCLE).addAll(allowedRoadTypes.get(VehicleType.WALKING));
		return allowedRoadTypes;
	}


	public static Set<WayType> getRoadTypes() {
		Set<WayType> roadTypes = new HashSet<>();
		roadTypes.add(WayType.RESIDENTIAL_ROAD);
		roadTypes.add(WayType.FOOTWAY);
		roadTypes.add(WayType.BRIDLEWAY);
		roadTypes.add(WayType.TRACK);
		roadTypes.add(WayType.PIER);
		roadTypes.add(WayType.SERVICE_ROAD);
		roadTypes.add(WayType.TERTIARY_ROAD);
		roadTypes.add(WayType.LIVING_STREET);
		roadTypes.add(WayType.SECONDARY_ROAD);
		roadTypes.add(WayType.UNCLASSIFIED_ROAD);
		roadTypes.add(WayType.PRIMARY_ROAD);
		roadTypes.add(WayType.PEDESTRIAN);
		roadTypes.add(WayType.RACEWAY);
		roadTypes.add(WayType.MOTORWAY);
		roadTypes.add(WayType.CYCLEWAY);
		return roadTypes;
	}



	private static Map<String, Integer> initSpeedLimitsFromTags() {
		Map<String, Integer> speedLimits = new HashMap<>();
		speedLimits.put("residential", 50);
		speedLimits.put("unclassified", 50);
		speedLimits.put("tertiary", 65);
		speedLimits.put("secondary", 80);
		speedLimits.put("trunk", 90);
		speedLimits.put("motorway", 130);
		return speedLimits;
	}
}
