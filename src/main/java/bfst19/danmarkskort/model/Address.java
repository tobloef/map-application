package bfst19.danmarkskort.model;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Address address = (Address) obj;
        return Float.compare(address.lat, lat) == 0 &&
                Float.compare(address.lon, lon) == 0 &&
                Objects.equals(streetName, address.streetName) &&
                Objects.equals(houseNumber, address.houseNumber) &&
                Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, streetName, houseNumber, city);
    }
}
