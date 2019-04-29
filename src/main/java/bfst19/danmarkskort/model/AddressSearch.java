package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.PlaceParsing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class AddressSearch {
    public static void test() {
        List<String> cities = getCities("Hf. Musikbyen");
    }

    public List<String> getRecommendations(String query) {
        return null;
    }

    public Address queryToAddress(String qeury) {
        return null;
    }

    public static List<Place> getPlaces(String streetName, String city) throws IOException, ClassNotFoundException {
        return PlaceParsing.loadPlaces(streetName, city);
    }

    public static List<String> getCities(String streetName) {
        List<String> list = new ArrayList<>();
        for (String str : PlaceParsing.getDecodedFileList()) {
            if (!str.toLowerCase().startsWith(streetName.toLowerCase() + "_")) {
                continue;
            }
            String substring = str.substring(str.indexOf("_") + 1);
            if (substring.equals("null")) {
                continue;
            }
            list.add(substring);
        }
        return list;
    }

    public static List<String> getStreets() {
        List<String> list = new ArrayList<>();
        for (String str : PlaceParsing.getDecodedFileList()) {
            String substring = str.substring(0, str.indexOf("_"));
            list.add(substring);
        }
        return list;
    }

    public static List<String> getStreets(String city) {
        List<String> list = new ArrayList<>();
        for (String str : PlaceParsing.getDecodedFileList()) {
            if (!str.toLowerCase().endsWith("_" + city.toLowerCase())) {
                continue;
            }
            String substring = str.substring(0, str.indexOf("_"));
            list.add(substring);
        }
        return list;
    }
}
