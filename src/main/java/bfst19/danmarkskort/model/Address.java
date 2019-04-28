package bfst19.danmarkskort.model;

import java.io.SequenceInputStream;
import java.io.Serializable;

public class Address implements Serializable {
    private String streetName;
    private String houseNumber;
    private String city;
    private String postCode;

    public Address(
            String streetName,
            String houseNumber,
            String city,
            String postCode
    ) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postCode = postCode;
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

    public String getPostCode() {
        return postCode;
    }
}
