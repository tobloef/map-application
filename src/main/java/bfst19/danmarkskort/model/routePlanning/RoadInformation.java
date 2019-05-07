package bfst19.danmarkskort.model.routePlanning;

import bfst19.danmarkskort.model.drawables.DrawableType;

import java.util.*;

public class RoadInformation {

    public static final Map<String, Integer> speedLimitsFromTags = initSpeedLimitsFromTags();
    public static final Set<DrawableType> roadTypes = getRoadTypes();
    public static final Map<VehicleType, EnumSet<DrawableType>> allowedRoadTypes = initAllowedRoadTypes();

    private static Map<VehicleType, EnumSet<DrawableType>> initAllowedRoadTypes() {
        Map<VehicleType, EnumSet<DrawableType>> allowedRoadTypes = new EnumMap<>(VehicleType.class);
        for (VehicleType vehicleType : VehicleType.values()) {
            allowedRoadTypes.put(vehicleType, EnumSet.noneOf(DrawableType.class));
        }
        allowedRoadTypes.get(VehicleType.CAR).add(DrawableType.RESIDENTIAL_ROAD);
        allowedRoadTypes.get(VehicleType.CAR).add(DrawableType.SERVICE_ROAD);
        allowedRoadTypes.get(VehicleType.CAR).add(DrawableType.TERTIARY_ROAD);
        allowedRoadTypes.get(VehicleType.CAR).add(DrawableType.LIVING_STREET);
        allowedRoadTypes.get(VehicleType.CAR).add(DrawableType.SECONDARY_ROAD);
        allowedRoadTypes.get(VehicleType.CAR).add(DrawableType.UNCLASSIFIED_ROAD);
        allowedRoadTypes.get(VehicleType.CAR).add(DrawableType.PRIMARY_ROAD);
        allowedRoadTypes.get(VehicleType.CAR).add(DrawableType.MOTORWAY);
        allowedRoadTypes.get(VehicleType.WALKING).addAll(getRoadTypes());
        allowedRoadTypes.get(VehicleType.WALKING).remove(DrawableType.MOTORWAY);
        allowedRoadTypes.get(VehicleType.BICYCLE).addAll(allowedRoadTypes.get(VehicleType.WALKING));
        return allowedRoadTypes;
    }


    public static Set<DrawableType> getRoadTypes() {
        Set<DrawableType> roadTypes = new HashSet<>();
        roadTypes.add(DrawableType.RESIDENTIAL_ROAD);
        roadTypes.add(DrawableType.FOOTWAY);
        roadTypes.add(DrawableType.BRIDLEWAY);
        roadTypes.add(DrawableType.TRACK);
        roadTypes.add(DrawableType.PIER);
        roadTypes.add(DrawableType.SERVICE_ROAD);
        roadTypes.add(DrawableType.TERTIARY_ROAD);
        roadTypes.add(DrawableType.LIVING_STREET);
        roadTypes.add(DrawableType.SECONDARY_ROAD);
        roadTypes.add(DrawableType.UNCLASSIFIED_ROAD);
        roadTypes.add(DrawableType.PRIMARY_ROAD);
        roadTypes.add(DrawableType.PEDESTRIAN);
        roadTypes.add(DrawableType.RACEWAY);
        roadTypes.add(DrawableType.MOTORWAY);
        roadTypes.add(DrawableType.CYCLEWAY);
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
