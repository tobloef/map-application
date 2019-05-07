package bfst19.danmarkskort.model.osmParsing;

import java.util.function.LongSupplier;

public class OSMNode implements LongSupplier {
    private final float lat;
	private final float lon;
    private final long id;

    public OSMNode(long id, float lon, float lat) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public long getAsLong() {
        return id;
    }
}
