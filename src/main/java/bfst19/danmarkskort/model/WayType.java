
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
	APRON,


	// Water
	WATER,
	SWIMMINGPOOL,
	CANAL,
	BASIN,

	//Navigable stuff that isnt roads.
	BRIDGE,

	// Roads
	RESIDENTIAL_ROAD,
	TERTIARY_ROAD,
	SERVICE_ROAD,
	UNCLASSIFIED_ROAD,
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
	FOOTWAY,
	BRIDLEWAY,
	CYCLEWAY,


	// Misc.
	UNKNOWN,
	POI


}
