package bfst19.danmarkskort.model;

import java.io.Serializable;

public class Place implements Serializable {
    private long id;
    private float lat;
    private float lon;
    private String placeName;
    private Address address;

    public Place(
            long id,
            float lat,
            float lon,
            String placeName,
            Address address
    ) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.placeName = placeName;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Address getAddress() {
        return address;
    }
}
