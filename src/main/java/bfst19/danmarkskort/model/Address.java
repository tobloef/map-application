package bfst19.danmarkskort.model;

import java.io.Serializable;

public class Address implements Serializable {
    private String streetName;
    private String houseNumber;
    private String floor;
    private String door;
    private String city;
    private String postCode;

    public Address() {}

    public Address(
            String streetName,
            String houseNumber,
            String floor,
            String door,
            String city,
            String postCode
    ) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.floor = floor;
        this.door = door;
        this.city = city;
        this.postCode = postCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getFloor() {
        return floor;
    }

    public String getDoor() {
        return door;
    }

    public String getCity() {
        return city;
    }

    public String getPostCode() {
        return postCode;
    }

    public static class Builder {
        private String streetName;
        private String houseNumber;
        private String floor;
        private String door;
        private String city;
        private String postCode;

        public Builder streetName(String streetName) {
            this.streetName = streetName;
            return this;
        }

        public Builder houseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder floor(String floor) {
            this.floor = floor;
            return this;
        }

        public Builder door(String door) {
            this.door = door;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder postCode(String postCode) {
            this.postCode = postCode;
            return this;
        }

        public Address build() {
            Address address = new Address();
            address.streetName = streetName;
            address.houseNumber = houseNumber;
            address.floor = floor;
            address.door = door;
            address.city = city;
            address.postCode = postCode;
            return address;
        }
    }
}
