package bfst19.osmdrawing;

import javafx.scene.paint.Color;

public enum WayType {
	COASTLINE(Color.GREEN,null ),
	WATER(Color.LIGHTSKYBLUE, null),
	ISLAND(Color.PINK, null),
	BARRIER(null, Color.BROWN),
	PITCH(Color.MEDIUMAQUAMARINE, null),
	SHRUB(Color.LIGHTGREEN, null),
	CANAL(Color.LIGHTSKYBLUE, null),
	FERRY(null, Color.BLUE, 2),
	TOUR(null, Color.BLACK, 2),
	BRIDGE(Color.DARKKHAKI, Color.DARKKHAKI),
	TREES(Color.DARKGREEN, null),
	GRASS(Color.LAWNGREEN, null),
	CONSTRUCTION(Color.DARKSEAGREEN, null),
	INDUSTRIAL(Color.THISTLE, null),
	CEMETERY(Color.OLIVEDRAB, null),
	RESIDENTIAL(Color.LAVENDER, null),
	BROWNFIELD(Color.TAN, null),
	ALLOTMENTS(Color.PALEGREEN, null),
	BASIN(Color.LIGHTSKYBLUE, null),
	PARK(Color.LIGHTGREEN, null),
	SWIMMINGPOOL(Color.LIGHTSKYBLUE, null),
	PLAYGROUND(Color.MEDIUMAQUAMARINE, null),
	BUILDING(Color.DARKGRAY, null),
	UNKNOWN(null, Color.BLACK),
	RAILWAY(null, Color.LIGHTGRAY, 2),
	PIER(Color.YELLOW, Color.YELLOW),
	IGNORE(null, null);

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
