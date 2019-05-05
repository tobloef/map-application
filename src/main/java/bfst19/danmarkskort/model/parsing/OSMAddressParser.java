package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.AddressData;

import java.util.*;
import java.util.stream.Collectors;

import static bfst19.danmarkskort.model.parsing.OSMTagKeys.*;
import static bfst19.danmarkskort.utils.Misc.getWithFallback;
import static bfst19.danmarkskort.utils.Misc.internIfNotNull;

public class OSMAddressParser {
    private List<Address> addresses = new ArrayList<>();
    private Set<String> cities = new HashSet<>();
    private Set<String> streetNames = new HashSet<>();

    public void tryAddAddress(Map<String, String> tags, OSMNode positionNode) {
        String streetName = internIfNotNull(getWithFallback(tags, streetNameKeys));
        String houseNumber = internIfNotNull(getWithFallback(tags, houseNumberKeys));
        String city = internIfNotNull(getWithFallback(tags, cityKeys));
        if (streetName == null) {
            return;
        }
        streetNames.add(streetName);
        if (city != null) {
            cities.add(city);
        }

        if (positionNode == null) {
            return;
        }
        float lat = positionNode.getLat();
        float lon = positionNode.getLon();
        Address address = new Address(lat, lon, streetName, houseNumber, city);
        addresses.add(address);
    }

    public AddressData createAddressData() {
        // Create list of addresses sorted by street name
        List<Address> addressesByStreetName = addresses.stream()
                .filter(a -> a.getStreetName() != null)
                .sorted(Comparator.comparing(a -> a.getStreetName().toLowerCase()))
                .collect(Collectors.toList());
        // Create list of addresses sorted by city name
        List<Address> addressesByCity = addresses.stream()
                .filter(a -> a.getCity() != null)
                .sorted(Comparator.comparing(a -> a.getCity().toLowerCase()))
                .collect(Collectors.toList());
        // Create list of cities, sorted alphabetically
        List<String> cityList = new ArrayList<>(cities);
        cityList.sort(String::compareToIgnoreCase);
        // Create list of street names, sorted alphabetically
        List<String> streetNameList = new ArrayList<>(streetNames);
        streetNameList.sort(String::compareToIgnoreCase);
        // Return the final address data
        return new AddressData(
                addressesByStreetName,
                addressesByCity,
                streetNameList,
                cityList
        );
    }

}
