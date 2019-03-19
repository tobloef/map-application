
package bfst19.osmdrawing.view;

import bfst19.osmdrawing.utils.ResourceLoader;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

//The wayTypes are drawed in the order they are written in
public enum WayType {
	COASTLINE(Color.web("EEF0D5"),null ),

	//Ø-ting
	ISLAND(Color.web("F4F0EA"), null),
	SCREE(Color.web("F1E5DC"),null),

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

	//Afkørelser
	TERTIARYROADEXIT(null, Color.web("FFFFFF"),0, 0.00008),
	SECONDARYROADEXIT(null, Color.web("F7FBBB"), 0, 0.00012),
	PRIMARYROADEXIT(null,Color.web("FFAD95"), 0, 0.00012),
	MOTORWAYEXIT(null, Color.web("FEB9C3"), 0, 0.00014),

	//Veje
	RESIDENTIALROAD(null, Color.web("FFFFFF"), 0, 0.00006),
	TERTIARYROAD(null,Color.web("FFFFFF"), 0, 0.00008),
	SERVICEROAD(null, Color.web("FFFFFF"),0,0.00004),
	UNCLASSIFIEDROAD(null,Color.web("FFFFFF"), 0, 0.00006),
	ROUNDABOUT(null, Color.web("FFFFFF"),0,0.00008),
	LIVING_STREET(null, Color.web("EDEEED"),0, 0.00006),
	SECONDARYROAD(null, Color.web("F7FBBB"),0, 0.00012),
	PRIMARYROAD(null, Color.web("FFAD95"), 0, 0.00012),
	RUNWAY(null, Color.web("BABACC"),0,0.0005), //Det ser mærkeligt ud?
	RAILWAY(null, Color.LIGHTGRAY, 2),
	PEDESTRIAN(null,Color.web("DDDDE9"), 0,0.00005),
	RACEWAY(null, Color.web(("FFBDC7")),0,0.00004),
	MOTORWAY(null, Color.web("FEB9C3"),0,0.00014),

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
	private double lineWidth;
	private Paint fill;
	private Color strokeColor;

	WayType(Paint fill, Color strokeColor) {
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.lineDash = 0;
		this.lineWidth = -1;
	}

	WayType(Paint fill, Color strokeColor, double lineDash) {
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.lineDash = lineDash;
		this.lineWidth = -1;
	}

	WayType(Paint fill, Color strokeColor, double lineDash, double roadWidth){
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.lineDash = lineDash;
		this.lineWidth = roadWidth;
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

	public boolean hasLineWidth(){ return (lineWidth != -1); }

	public double getLineWidth() {
		return lineWidth;
	}

}
