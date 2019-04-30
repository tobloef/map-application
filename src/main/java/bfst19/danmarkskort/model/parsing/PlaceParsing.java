package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.Place;
import bfst19.danmarkskort.utils.ResourceLoader;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static bfst19.danmarkskort.utils.FileUtils.deleteDirectory;

public class PlaceParsing {
    private static final String directory = "data/places/";

    private static Map<String, ObjectOutputStream> streetOutputStreams = new HashMap<>();
    private static List<String> fileListCache;

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
        fileListCache = null;
    }

    public static List<Place> loadPlaces(String street, String city) throws IOException, ClassNotFoundException {
        List<Place> places = new ArrayList<>();
        String key = getKey(street, city);
        String path = getPath(key);
        File file = new File(path);
        if (!file.exists()) {
            return places;
        }
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
        return places;
    }

    private static String getPath(String key) {
        String filename = Base64.getUrlEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
        String path = ResourceLoader.fixPath(directory + filename + ".ser");
        return path;
    }

    private static String getKey(String street, String city) {
        String key = street + "_"+ city;
        //key = key.toLowerCase();
        return key;
    }

    public static void closeStreams() throws IOException {
        for (ObjectOutputStream stream : streetOutputStreams.values()) {
            stream.close();
        }
        streetOutputStreams.clear();
    }

    public static void removeSavedPlaces() {
        deleteDirectory(directory);
    }

    public static List<String> getFileList() {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(directory);
        File[] fileArray = folder.listFiles();
        if (fileArray == null) {
            return fileNames;
        }
        for (File aFileArray : fileArray) {
            if (aFileArray.isFile()) {
                fileNames.add(aFileArray.getName());
            }
        }
        return fileNames;
    }

    public static List<String> getDecodedFileList() {
        if (fileListCache == null) {
            fileListCache = new ArrayList<>();
            Base64.Decoder decoder = Base64.getUrlDecoder();
            for (String str : getFileList()) {
                String replace = str.replace(".ser", "");
                byte[] bytes = decoder.decode(replace);
                String s = new String(bytes, StandardCharsets.UTF_8);
                fileListCache.add(s);
            }
        }
        return fileListCache;
    }
}
