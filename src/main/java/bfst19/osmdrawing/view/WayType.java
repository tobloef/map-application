
package bfst19.osmdrawing.view;

import bfst19.osmdrawing.utils.ResourceLoader;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

//The wayTypes are drawed in the order they are written in
public enum WayType {
	COASTLINE(Color.web("EEF0D5"),null, true ),

	//Ø-ting
	ISLAND(Color.web("F4F0EA"), null, true),
	SCREE(Color.web("F1E5DC"),null, true),

	//Natur
	FAKE(new ImagePattern(new Image(ResourceLoader.getResourceAsStream("view/forest-texture.jpg")), 0,0, 0.1, 0.1, true), null),
	// The above is an example of how fill images are used. An image to be used as texture, x and y are likely
	// uninmportant for us, width and height are the scale of the image (smaller number = smaller image), and propotional
	// is whether or not the image is proportional
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
	private Paint fill;
	private Color strokeColor;
	private boolean alwaysDraw;




	WayType(Paint fill, Color strokeColor) {
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.lineDash = 0;
		alwaysDraw = false;
	}

	WayType(Paint fill, Color strokeColor, boolean alwaysDraw) {
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.lineDash = 0;
		this.alwaysDraw = alwaysDraw;
	}

	WayType(Paint fill, Color strokeColor, double lineDash) {
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.lineDash = lineDash;
	}

	public double getLineDash(){
		return this.lineDash;
	}

	public Paint getFill() {
		return fill;
	}

	public boolean hasFill(){
		return (fill != null);
	}

	public boolean hasStroke(){
		return (strokeColor != null);
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public boolean alwaysDraw() {
		return alwaysDraw;
	}
}
