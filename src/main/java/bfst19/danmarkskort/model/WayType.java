
package bfst19.danmarkskort.model;

import java.util.ArrayList;
import java.util.List;

// The wayTypes are drawn in the order they are written in
@SuppressWarnings("unused")
public enum WayType {
	COASTLINE,

	// Islands
	ISLAND,
	SCREE,

	ADMINISTRATIVE,
	RESIDENTIAL,

	// Water
	TREES,
	GRASS,
	SAND,
	FARMLAND,
	WOOD,
	HEATH,
	MEADOW,
	PARK,
	WETLAND, //TODO: Skal have en texture
	BROWNFIELD,
	SHRUB,

	// Buildings
	CONSTRUCTION,
	INDUSTRIAL,
	CEMETERY,
	ALLOTMENTS,
	PLAYGROUND,
	BUILDING,
	BARRIER,
	PITCH,
	GROYNE,
	PIER,
	BRIDGE,
	APRON,

	// Exits
	TERTIARY_ROAD_EXIT,
	SECONDARY_ROAD_EXIT,
	PRIMARY_ROAD_EXIT,
	MOTORWAY_EXIT,

	// Roads
	RESIDENTIAL_ROAD,
	TERTIARY_ROAD,
	SERVICE_ROAD,
	UNCLASSIFIED_ROAD,
	ROUNDABOUT,
	LIVING_STREET,
	SECONDARY_ROAD,
	PRIMARY_ROAD,
	RUNWAY, //TODO:Det ser mærkeligt ud?
	RAILWAY,
	PEDESTRIAN,
	RACEWAY,
	MOTORWAY,

	// Abstract Paths
	MARKING, //TODO: Skal have en tykkelse
	TRACK,
	FERRY,
	TOUR,
	BUS_GUIDEWAY,
	FOOTWAY,
	BRIDLEWAY,
	CYCLEWAY,

	// Water
	WATER,
	SWIMMINGPOOL,
	CANAL,
	BASIN,

	// Misc.
	UNKNOWN,
	;

	public static List<WayType> getRoadTypes() {
		List<WayType> roadTypes = new ArrayList<>();
		roadTypes.add(RESIDENTIAL_ROAD);
		roadTypes.add(SERVICE_ROAD);
		roadTypes.add(TERTIARY_ROAD);
		roadTypes.add(UNCLASSIFIED_ROAD);
		roadTypes.add(ROUNDABOUT);
		roadTypes.add(LIVING_STREET);
		roadTypes.add(SECONDARY_ROAD);
		roadTypes.add(PRIMARY_ROAD);
		roadTypes.add(RAILWAY);
		roadTypes.add(PEDESTRIAN);
		roadTypes.add(RACEWAY);
		roadTypes.add(MOTORWAY);
		return roadTypes;
	}
}
