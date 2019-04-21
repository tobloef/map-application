package bfst19.danmarkskort.model;

import java.util.*;

public class RoadInformation {

	public static Map<String, Integer> speedLimitsFromTags = initSpeedLimitsFromTags();
	public static Set<WayType> roadTypes = getRoadTypes();



	public static Set<WayType> getRoadTypes() {
		Set<WayType> roadTypes = new HashSet<>();
		roadTypes.add(WayType.RESIDENTIAL_ROAD);
		roadTypes.add(WayType.SERVICE_ROAD);
		roadTypes.add(WayType.TERTIARY_ROAD);
		roadTypes.add(WayType.LIVING_STREET);
		roadTypes.add(WayType.SECONDARY_ROAD);
		roadTypes.add(WayType.UNCLASSIFIED_ROAD);
		roadTypes.add(WayType.ROUNDABOUT);
		roadTypes.add(WayType.PRIMARY_ROAD);
		roadTypes.add(WayType.PEDESTRIAN);
		roadTypes.add(WayType.RACEWAY);
		roadTypes.add(WayType.MOTORWAY);
		roadTypes.add(WayType.TERTIARY_ROAD_EXIT);
		roadTypes.add(WayType.SECONDARY_ROAD_EXIT);
		roadTypes.add(WayType.PRIMARY_ROAD_EXIT);
		roadTypes.add(WayType.MOTORWAY_EXIT);
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
