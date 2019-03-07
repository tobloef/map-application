package bfst19.osmdrawing.model;

import java.util.function.LongSupplier;

public class OSMNode implements LongSupplier {
	private float lat, lon;
	private long id;

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public OSMNode(long id, float lon, float lat) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}

	public long getAsLong() {
		return id;
	}
}
