package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.Place;
import bfst19.danmarkskort.utils.ResourceLoader;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static bfst19.danmarkskort.utils.FileUtils.deleteDirectory;

public class PlaceParsing {
    private static final String directory = "data/places/";

    private static Map<String, ObjectOutputStream> streetOutputStreams = new HashMap<>();

    public static void savePlace(Place place) throws IOException {
        Address address = place.getAddress();
        String key = getKey(address.getStreetName(), address.getCity());
        if (!streetOutputStreams.containsKey(key)) {
            String path = getPath(key);
            File file = new File(path);
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            ObjectOutputStream outputStream = new ObjectOutputStream(bufferedOutputStream);
            streetOutputStreams.put(key, outputStream);
        }
        streetOutputStreams.get(key).writeObject(place);
    }

    public static Place[] loadPlaces(String street, String city) throws IOException, ClassNotFoundException {
        List<Place> places = new ArrayList<>();
        String key = getKey(street, city);
        String path = getPath(key);
        InputStream inputStream = new FileInputStream(path);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        try (ObjectInputStream input = new ObjectInputStream(bufferedInputStream)) {
            while (true) {
                try {
                    Place place = (Place) input.readObject();
                    places.add(place);
                } catch (EOFException e) {
                    break;
                }
            }
        }
        return places.toArray(new Place[0]);
    }

    private static String getPath(String key) {
        String filename = Base64.getUrlEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
        String path = ResourceLoader.fixPath(directory + filename + ".ser");
        return path;
    }

    private static String getKey(String street, String city) {
        return street + "_"+ city;
    }

    public static void closeStreams() {
        for (ObjectOutputStream stream : streetOutputStreams.values()) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        streetOutputStreams.clear();
    }

    public static void removeSavedPlaces() {
        deleteDirectory(directory);
    }
}
