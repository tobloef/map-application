package bfst19.danmarkskort.model.address;

import java.util.List;

public class AddressData {
    private final List<Address> addressesByStreetName;
    private final List<Address> addressesByCity;
    private final List<String> cities;
    private final List<String> streetNames;

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
