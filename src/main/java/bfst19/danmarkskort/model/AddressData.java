package bfst19.danmarkskort.model;

import java.util.List;

public class AddressData {
    private List<Address> addressesByStreetName;
    private List<Address> addressesByCity;
    private List<String> cities;
    private List<String> streetNames;

    public AddressData(
            List<Address> addressesByStreetName,
            List<Address> addressesByCity,
            List<String> streetNames,
            List<String> cities
    ) {
        this.addressesByStreetName = addressesByStreetName;
        this.addressesByCity = addressesByCity;
        this.cities = cities;
        this.streetNames = streetNames;
    }

    public List<Address> getAddressesByStreetName() {
        return addressesByStreetName;
    }

    public List<Address> getAddressesByCity() {
        return addressesByCity;
    }

    public List<String> getCities() {
        return cities;
    }

    public List<String> getStreetNames() {
        return streetNames;
    }
}
