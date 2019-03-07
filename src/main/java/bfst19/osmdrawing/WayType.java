
package bfst19.osmdrawing;

import javafx.scene.paint.Color;
//The wayTypes are drawed in the order they are written in
public enum WayType {
	COASTLINE(Color.web("EEF0D5"),null ),

	//Ø-ting
	ISLAND(Color.web("F4F0EA"), null),
	SCREE(Color.web("F1E5DC"),null),

	//Natur
	TREES(Color.DARKGREEN, null),
	GRASS(Color.web("C4EDB2"), null),
	SAND(Color.web("FFF1BA"), null),
	FARMLAND(Color.web("EFF1D5"), null),
	WOOD(Color.web("A1D29F"), null),
	HEATH(Color.web("D6D99F"),null),
	MEADOW(Color.web("C4EDB2"), null),
	PARK(Color.LIGHTGREEN, null),
	WETLAND(Color.LIGHTBLUE, null), //Skal have en tekstur
	BROWNFIELD(Color.TAN, null),
	SHRUB(Color.LIGHTGREEN, null),

	//Bygningsting?
	CONSTRUCTION(Color.DARKSEAGREEN, null),
	INDUSTRIAL(Color.THISTLE, null),
	CEMETERY(Color.OLIVEDRAB, null),
	RESIDENTIAL(Color.LAVENDER, null),
	ALLOTMENTS(Color.PALEGREEN, null),
	PLAYGROUND(Color.MEDIUMAQUAMARINE, null),
	BUILDING(Color.DARKGRAY, null),
	BARRIER(null, Color.BROWN),
	PITCH(Color.MEDIUMAQUAMARINE, null),
	GROYNE(Color.web("A9A9A9"), null),
	PIER(Color.YELLOW, Color.YELLOW),
	BRIDGE(Color.DARKKHAKI, Color.DARKKHAKI),
	APRON(Color.web("DADAE0"),null),

	//Veje
	RESIDENTIALROAD(null, Color.web("FFFFFF")), //Skal have en tykkelse
	PRIMARYROAD(null, Color.web("FFAD95")), //Skal have en tykkelse
	RUNWAY(null, Color.web("BABACC")), //Skal have en tykkelse
	RAILWAY(null, Color.LIGHTGRAY, 2),
	MOTORWAY(null, Color.web("FEB9C3")), //Skal have en tykkelse
	SECONDARYROAD(null, Color.web("F7FBBB")), //Skal have en tykkelse
	TERTIARYROAD(null,Color.web("FFFFFF")), //Skal have en tykkelse
	SERVICEROAD(null, Color.web("FFFFFF")), //Skal have en tykkelse
	UNCLASSIFIEDROAD(null,Color.web("FFFFFF")), //Skal have en tykkelse
	LIVING_STREET(null, Color.web("EDEEED")), //Skal have en tykkelse
	PEDESTRIAN(null,Color.web("DDDDE9")), //Skal have en tykkelse
	RACEWAY(null, Color.web(("FFBDC7"))), //Skal have en tykkelse
	ROUNDABOUT(null, Color.web("FFFFFF")), //Skal have en tykkelse

	//Afkørelser
	MOTORWAYEXIT(null, Color.web("FC8A9C")), //Skal have en tykkelse
	PRIMARYROADEXIT(null,Color.web("FFAD95")), //Skal have en tykkelse
	SECONDARYROADEXIT(null, Color.web("F7FBBB")), //Skal have en tykkelse
	TERTIARYROADEXIT(null, Color.web("FFFFFF")), //Skal have en tykkelse

	//Abstrakte ting såsom ruter
	MARKING(null, Color.BLACK), //Skal have en tykkelse
	TRACK(null, Color.web("B68235"), 2),
	FERRY(null, Color.BLUE, 2),
	TOUR(null, Color.BLACK, 2),
	BUS_GUIDEWAY(null, Color.web("6066FF"), 2),
	FOOTWAY(null, Color.web("FE9E93"),2),
	BRIDLEWAY(null, Color.web("A8D7B6"), 2),
	CYCLEWAY(null, Color.web("7B7EF8"), 2),

	//Vand
	WATER(Color.LIGHTSKYBLUE, null),
	SWIMMINGPOOL(Color.LIGHTSKYBLUE, null),
	CANAL(Color.LIGHTSKYBLUE, null),
	BASIN(Color.LIGHTSKYBLUE, null),

	IGNORE(null, null),
	UNKNOWN(null, Color.BLACK);


	private double lineDash;
	private Color fillColor;
	private Color strokeColor;





	WayType(Color fillColor, Color strokeColor) {
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.lineDash = 0;
	}

	WayType(Color fillColor, Color strokeColor, double lineDash) {
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.lineDash = lineDash;
	}

	public double getLineDash(){
		return this.lineDash;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public boolean hasFill(){
		return (fillColor != null);
	}

	public boolean hasStroke(){
		return (strokeColor != null);
	}

	public Color getStrokeColor() {
		return strokeColor;
	}
}
