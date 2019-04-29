package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.AddressParser;
import bfst19.danmarkskort.model.parsing.PlaceParsing;

import java.io.IOException;
import java.util.*;

public class AddressSearch {
    private static List<String> fileList;

    public static Place test() {
        try {
            Address address = queryToAddress("Amagerbrogade 2, 709");
            List<String> cities = getCities(address.getStreetName());
            String city = cities.get(0);
            List<Place> places = getPlaces(address.getStreetName(), city);
            for (Place place : places) {
                Address newAddress = place.getAddress();
                boolean streetNamesMatch = Objects.equals(newAddress.getStreetName(), address.getStreetName());
                boolean cityMatch = Objects.equals(newAddress.getCity(), address.getStreetName());
                boolean houseNumberMatch = Objects.equals(newAddress.getHouseNumber(), address.getHouseNumber());
                if (streetNamesMatch && cityMatch && houseNumberMatch) {
                    System.out.println(address);
                    System.out.println(newAddress);
                    return place;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> getRecommendations(String query) {
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
    }
}
