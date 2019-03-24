package bfst19.osmdrawing.model;

import bfst19.osmdrawing.model.parsing.OSMWay;

public class Road extends Polyline {
	String Restrictions; //Change to proper type later / Split later.
	public Road(OSMWay way) {
		super(way);
	}
}
