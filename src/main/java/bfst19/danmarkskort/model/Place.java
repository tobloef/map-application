package bfst19.danmarkskort.model;

public class Place {
    private long id;
    private float lat;
    private float lon;
    private String placeName;
    private String streetName;
    private String houseNumber;
    private String city;
    private String postCode;

    public Place(
            long id,
            float lat,
            float lon,
            String placeName,
            String streetName,
            String houseNumber,
            String city,
            String postCode
    ) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.placeName = placeName;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postCode = postCode;
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

    public static class Builder {
        private long id;
        private float lat;
        private float lon;
        private String placeName;
        private String streetName;
        private String houseNumber;
        private String city;
        private String postCode;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder lat(float lat) {
            this.lat = lat;
            return this;
        }

        public Builder lon(float lon) {
            this.lon = lon;
            return this;
        }

        public Builder placeName(String placeName) {
            this.placeName = placeName;
            return this;
        }

        public Builder streetName(String streetName) {
            this.streetName = streetName;
            return this;
        }

        public Builder houseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
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


        public Place build() {
            return new Place(
                    id,
                    lat,
                    lon,
                    placeName,
                    streetName,
                    houseNumber,
                    city,
                    postCode
            );
        }
    }
}
