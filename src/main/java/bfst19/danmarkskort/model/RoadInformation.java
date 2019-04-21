package bfst19.danmarkskort.model;

import java.util.HashMap;
import java.util.Map;

public class RoadInformation {

	public static Map<String, Integer> speedLimitsFromTags = initSpeedLimitsFromTags();

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
