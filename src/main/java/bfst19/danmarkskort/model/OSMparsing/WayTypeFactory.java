package bfst19.danmarkskort.model.OSMparsing;

import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.utils.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

import static bfst19.danmarkskort.utils.EnumHelper.stringToWayType;

@SuppressWarnings("unchecked")
public class WayTypeFactory {
    private static final String wayTypesConfigPath = "rs:config/spartan.yaml";

    /**
     * Maps cartographic feature keys to maps that map feature values to DrawableType.
     * Uses null feature value to indicate the key has no values, but still correspond to a DrawableType.
     */
    private static Map<String, Map<String, DrawableType>> keyToValueWayTypeMap;

    /***
     * Get the DrawableType for a given OSM key and value.
     * @param key Key of the cartographic feature
     * @param value Optional value of the feature
     * @return The corresponding DrawableType
     */
    public static DrawableType getWayType(String key, String value) {
        if (keyToValueWayTypeMap == null) {
            initializeWayTypes();
        }
        if (keyToValueWayTypeMap.containsKey(key)) {
            Map<String, DrawableType> values = keyToValueWayTypeMap.get(key);

            if (values.containsKey(value)) {
                return values.get(value);
            } else if (values.containsKey(null)) {
                return values.get(null);
            }
        }
        return null;
    }

    /**
     * Initialize the map of WayTypes by reading a config file from disk.
     */
    private static void initializeWayTypes() {
        keyToValueWayTypeMap = new HashMap<>();
        // Read the YAML file
        Yaml yaml = new Yaml();
        InputStream inputStream = ResourceLoader.getResourceAsStream(wayTypesConfigPath);
        List<Map> wayTypeMaps = yaml.load(inputStream);

        // Read each DrawableType in the file
        for (Map wayTypeMap : wayTypeMaps) {
            for (Object wayTypeEntryObj : wayTypeMap.entrySet()) {
                try {
                    Map.Entry<String, Object> wayTypeEntry = (Map.Entry<String, Object>) wayTypeEntryObj;
                    parseWayTypeEntry(wayTypeEntry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void parseWayTypeEntry(Map.Entry<String, Object> entry) throws Exception {
        // Get DrawableType
        String wayTypeStr = entry.getKey();
        DrawableType drawableType = stringToWayType(entry.getKey());
        if (drawableType == null) {
            throw new Exception("Wrong/Missing DrawableType: " + wayTypeStr);
        }
        // Load the keys
        ArrayList<Object> keys = (ArrayList<Object>) entry.getValue();
        for (Object keyObj : keys) {
            String key = null;
            List<String> values = null;
            // Check if the key is just a string, or if it maps to a list of values
            if (keyObj instanceof String) {
                key = (String) keyObj;
            } else if (keyObj instanceof LinkedHashMap) {
                LinkedHashMap<String, Object> subTagMap = (LinkedHashMap<String, Object>) keyObj;
                for (Object valueObj : subTagMap.entrySet()) {
                    Map.Entry<String, ArrayList<String>> tagSubTagEntry = (Map.Entry<String, ArrayList<String>>) valueObj;
                    key = tagSubTagEntry.getKey();
                    values = tagSubTagEntry.getValue();
                }
            } else {
                throw new Exception("Invalid type for key: " + keyObj.getClass());
            }
            // Add the DrawableType, key and possible values to the map.
            addValues(drawableType, key, values);
        }
    }

    private static void addValues(DrawableType drawableType, String key, List<String> values) {
        if (!keyToValueWayTypeMap.containsKey(key)) {
            keyToValueWayTypeMap.put(key, new HashMap<>());
        }
        if (values != null && values.size() > 0) {
            for (String value : values) {
                keyToValueWayTypeMap.get(key).put(value, drawableType);
            }
        } else {
            // Default case for when there's no values, only key.
            keyToValueWayTypeMap.get(key).put(null, drawableType);
        }
    }
}
