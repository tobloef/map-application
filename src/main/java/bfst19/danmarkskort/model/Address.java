package bfst19.danmarkskort.model;

import java.io.Serializable;

public class Address implements Serializable {
    private float lat;
    private float lon;
    private String streetName;
    private String houseNumber;
    private String city;

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

    @Override
    public String toString() {
        return streetName + " " + houseNumber + ", " + city;
    }
}
