
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
	CONSTRUCTION(0.003036274142973914, Color.DARKSEAGREEN, null),
	INDUSTRIAL(0.003036274142973914, Color.THISTLE, null),
	CEMETERY(0.003036274142973914, Color.OLIVEDRAB, null),
	RESIDENTIAL(0.003036274142973914, Color.LAVENDER, null),
	ALLOTMENTS(0.003036274142973914, Color.PALEGREEN, null),
	PLAYGROUND(0.003036274142973914,Color.MEDIUMAQUAMARINE, null),
	BUILDING(0.003036274142973914, Color.DARKGRAY, null),
	BARRIER(null, Color.BROWN),
	PITCH(0.003036274142973914,Color.MEDIUMAQUAMARINE, null),
	GROYNE(0.003036274142973914, Color.web("A9A9A9"), null),
	PIER(0.003036274142973914, Color.YELLOW, Color.YELLOW),
	BRIDGE(Color.DARKKHAKI, Color.DARKKHAKI),
	APRON(0.003036274142973914, Color.web("DADAE0"),null),

	//Afkørelser
	TERTIARYROADEXIT(3.9127643402560167E-4, null, Color.web("FFFFFF"),0, 0.00008),
	SECONDARYROADEXIT(0.0021356850333129405, null, Color.web("F7FBBB"), 0, 0.00012),
	PRIMARYROADEXIT(0.0021356850333129405, null,Color.web("FFAD95"), 0, 0.00012),
	MOTORWAYEXIT(0.005420230931152298, null, Color.web("FEB9C3"), 0, 0.00014),

	//Veje
	RESIDENTIALROAD(4.2504755435936523E-4,null, Color.web("FFFFFF"), 0, 0.00006),
	TERTIARYROAD(3.9127643402560167E-4, null,Color.web("FFFFFF"), 0, 0.00008),
	SERVICEROAD(6.570464146288649E-5, null, Color.web("FFFFFF"),0,0.00004),
	UNCLASSIFIEDROAD(4.2504755435936523E-4, null,Color.web("FFFFFF"), 0, 0.00006),
	ROUNDABOUT(1.8573734058326716E-4, null, Color.web("FFFFFF"),0,0.00008),
	LIVING_STREET(4.2504755435936523E-4,null, Color.web("EDEEED"),0, 0.00006),
	SECONDARYROAD(0.0021356850333129405,null, Color.web("F7FBBB"),0, 0.00012),
	PRIMARYROAD(0.0021356850333129405,null, Color.web("FFAD95"), 0, 0.00012),
	RUNWAY(0.002853485097237559,null, Color.web("BABACC"),0,0.0005), //Det ser mærkeligt ud?
	RAILWAY(0.002853485097237559, null, Color.LIGHTGRAY, 2),
	PEDESTRIAN(4.2504755435936523E-4, null,Color.web("DDDDE9"), 0,0.00005),
	RACEWAY(6.570464146288649E-5, null, Color.web(("FFBDC7")),0,0.00004),
	MOTORWAY(0.005420230931152298, null, Color.web("FEB9C3"),0,0.00014),

	//Abstrakte ting såsom ruter
	MARKING(5.797815402773365E-4,null, Color.BLACK), //Skal have en tykkelse
	TRACK(5.797815402773365E-4, null, Color.web("B68235"), 2),
	FERRY(5.797815402773365E-4, null, Color.BLUE, 2),
	TOUR(5.797815402773365E-4, null, Color.BLACK, 2),
	BUS_GUIDEWAY(5.797815402773365E-4, null, Color.web("6066FF"), 2),
	FOOTWAY(5.797815402773365E-4,null, Color.web("FE9E93"),2),
	BRIDLEWAY(5.797815402773365E-4,null, Color.web("A8D7B6"), 2),
	CYCLEWAY(5.797815402773365E-4,null, Color.web("7B7EF8"), 2),

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
	private boolean alwaysDraw;

	private double zoomLevel = 4;

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
		this.lineWidth = -1;
	}

	WayType(Paint fill, Color strokeColor, double lineDash) {
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.lineDash = lineDash;
		this.lineWidth = -1;
	}

	WayType(Paint fill, Color strokeColor, double lineDash, double lineWidth){
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.lineDash = lineDash;
		this.lineWidth = lineWidth;
	}

	WayType(double zoomLevel, Paint fill, Color strokeColor, double lineDash, double lineWidth) {
		this.lineDash = lineDash;
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.zoomLevel = zoomLevel;
		this.lineWidth = lineWidth;
	}

	WayType(double zoomLevel, Paint fill, Color strokeColor, double lineDash) {
		this.lineDash = lineDash;
		this.fill = fill;
		this.strokeColor = strokeColor;
		this.zoomLevel = zoomLevel;
	}

	WayType(double zoomLevel, Paint fill, Color strokeColor) {
		this.zoomLevel = zoomLevel;
		this.fill = fill;
		this.strokeColor = strokeColor;
	}

	public double getLineDash(){
		return this.lineDash;
	}

	public Paint getFill() {
		return fill;
	}

	public double getZoomLevel() {
		return zoomLevel;
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
	public boolean hasLineWidth(){ return (lineWidth != -1); }

	public double getLineWidth() {
		return lineWidth;
	}

}
