package bfst19.danmarkskort.model.parsing;

public class OSMTagKeys {
    final public static String[] streetNameKeys = new String[]{
            "addr:street",
            "osak:street_name",
    };

    final public static  String[] nameKeys = new String[]{
            "name",
    };

    final public static  String[] houseNumberKeys = new String[]{
            "addr:housenumber",
            "osak:house_no",
    };

    final public static  String[] floorKeys = new String[]{
            "addr:floor",
    };

    final public static  String[] doorKeys = new String[]{
            "addr:door",
    };

    final public static  String[] cityKeys = new String[]{
            "addr:city",
            "is_in:city",
            "osak:municipality_name",
            "is_in",
    };

    final public static  String[] postCodeKeys = new String[]{
            "addr:postcode",
    };
}
