package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.AddressParser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AddressSearch {
    private static List<String> fileList;

    public static List<Address> getRecommendations(List<Address> addresses, String stringQuery) {
        AddressQuery addressQuery = AddressParser.parse(addresses, stringQuery);
        if (addressQuery == null) {
            return null;
        }
        if (addressQuery.getStreetName() == null) {
            return null;
        }
        List<Address> matches = getAddressesWithStreetName(addresses, addressQuery.getStreetName());
        matches = matches.stream()
                .filter(a -> Objects.equals(a.getCity(), addressQuery.getCity()))
                .filter(a -> Objects.equals(a.getHouseNumber(), addressQuery.getHouseNumber()))
                .collect(Collectors.toList());
        return matches;
    }

    public static List<Address> getAddressesWithStreetName(List<Address> addresses, String streetName) {
        int low = 0;
        int high = addresses.size();
        int middle = 0;

        while (high - low > 1) {
            middle = low + (high - low) / 2;
            String middleStr = addresses.get(middle).getStreetName();
            int compare = streetName.compareToIgnoreCase(middleStr);
            if (compare == 0) {
                break;
            } else if (compare < 0) {
                high = middle;
            } else {
                low = middle;
            }
        }
        low = middle;
        while (low > 0) {
            if (!Objects.equals(addresses.get(low - 1).getStreetName(), streetName)) {
                break;
            }
            low--;
        }
        high = middle;
        while (high + 2 < addresses.size()) {
            if (!Objects.equals(addresses.get(high + 1).getStreetName(), streetName)) {
                break;
            }
            high++;
        }
        return addresses.subList(low, high + 1);
    }


    /*public static Place test() {
        try {
            Address address = queryToAddress("Amagerbrogade 2, 709");
            List<String> cities = getCities(address.getStreetName());
            address.setCity(cities.get(0));
            List<Place> places = getPlaces(address.getStreetName(), address.getCity());
            for (Place place : places) {
                Address newAddress = place.getAddress();
                if (isAddressMatch(address, newAddress)) {
                    return place;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean isAddressMatch(Address address, Address newAddress) {
        boolean streetNamesMatch = Objects.equals(
                newAddress.getStreetName().toLowerCase(),
                address.getStreetName().toLowerCase()
        );
        boolean cityMatch = Objects.equals(
                newAddress.getCity().toLowerCase(),
                address.getCity().toLowerCase()
        );
        boolean houseNumberMatch = Objects.equals(
                newAddress.getHouseNumber().toLowerCase(),
                address.getHouseNumber().toLowerCase()
        );
        return streetNamesMatch && cityMatch && houseNumberMatch;
    }

    public static List<String> getRecommendations(String query) {
        List<String> recommendations = new ArrayList<>();
        Address address = queryToAddress(query);

        //Get streets that start with query.
        //Get streets that match parsed and add house numbers.
        //Get cities that match parsed.
        //Read post code and recommend city.
        //If matches city, get streets in city.
        return null;
    }

    public static Address queryToAddress(String query) {
        return AddressParser.parse(query);
    }

    public static List<Place> getPlaces(String streetName, String city) throws IOException, ClassNotFoundException {
        return PlaceParsing.loadPlaces(streetName, city);
    }

    public static List<String> getCities(String streetName) {
        Set<String> set = new HashSet<>();
        for (String str : PlaceParsing.getDecodedFileList()) {
            if (!str.toLowerCase().startsWith(streetName.toLowerCase() + "_")) {
                continue;
            }
            String substring = str.substring(str.indexOf("_") + 1);
            if (substring.equals("null")) {
                continue;
            }
            set.add(substring);
        }
        return new ArrayList<>(set);
    }

    public static List<String> getStreets() {
        Set<String> set = new HashSet<>();
        for (String str : PlaceParsing.getDecodedFileList()) {
            String substring = str.substring(0, str.indexOf("_"));
            set.add(substring);
        }
        return new ArrayList<>(set);
    }

    public static List<String> getStreets(String city) {
        Set<String> set = new HashSet<>();
        for (String str : PlaceParsing.getDecodedFileList()) {
            if (!str.toLowerCase().endsWith("_" + city.toLowerCase())) {
                continue;
            }
            String substring = str.substring(0, str.indexOf("_"));
            set.add(substring);
        }
        return new ArrayList<>(set);
    }*/
}
