package bfst19.danmarkskort.data;

import bfst19.danmarkskort.model.address.Address;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AddressTestData {
    public abstract List<Address> getAddresses();

    public List<Address> getAddressesByStreetName() {
        return getAddresses().stream()
                .sorted(Comparator.comparing(Address::getStreetName))
                .collect(Collectors.toList());
    }

    public List<Address> getAddressesByCity() {
        return getAddresses().stream()
                .sorted(Comparator.comparing(Address::getCity))
                .collect(Collectors.toList());
    }

    public List<String> getCities() {
        return getAddresses().stream()
                .map(Address::getCity)
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }

    public List<String> getStreetNames() {
        return getAddresses().stream()
                .map(Address::getStreetName)
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }
}
