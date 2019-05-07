package bfst19.danmarkskort.model;

import java.io.Serializable;

public class Address implements Serializable {
    private final float lat;
    private final float lon;
    private final String streetName;
    private final String houseNumber;
    private final String city;

    public Address(
            float lat,
            float lon,
            String streetName,
            String houseNumber,
            String city
    ) {
        this.lat = lat;
        this.lon = lon;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getCity() {
        return city;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return streetName + " " + houseNumber + ", " + city;
    }
}
