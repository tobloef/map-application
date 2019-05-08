package bfst19.danmarkskort.model.osmParsing;

import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.utils.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

import static bfst19.danmarkskort.utils.EnumHelper.stringToDrawableType;

@SuppressWarnings("unchecked")
public class DrawableTypeFactory {
    private static final String drawableTypesConfigPath = "rs:config/spartan.yaml";

    /**
     * Maps cartographic feature keys to maps that map feature values to DrawableType.
     * Uses null feature value to indicate the key has no values, but still correspond to a DrawableType.
     */
    private static Map<String, Map<String, DrawableType>> keyToValueDrawableTypeMap;

    /***
     * Get the DrawableType for a given OSM key and value.
     * @param key Key of the cartographic feature
     * @param value Optional value of the feature
     * @return The corresponding DrawableType
     */
    public static DrawableType getDrawableType(String key, String value) {
        if (keyToValueDrawableTypeMap == null) {
            initializeDrawableTypes();
        }
        if (keyToValueDrawableTypeMap.containsKey(key)) {
            Map<String, DrawableType> values = keyToValueDrawableTypeMap.get(key);

            if (values.containsKey(value)) {
                return values.get(value);
            } else if (values.containsKey(null)) {
                return values.get(null);
            }
        }
        return null;
    }

    /**
     * Initialize the map of DrawableTypes by reading a config file from disk.
     */
    private static void initializeDrawableTypes() {
        keyToValueDrawableTypeMap = new HashMap<>();
        // Read the YAML file
        Yaml yaml = new Yaml();
        InputStream inputStream = ResourceLoader.getResourceAsStream(drawableTypesConfigPath);
        List<Map> drawableTypeMaps = yaml.load(inputStream);

        // Read each DrawableType in the file
        for (Map drawableTypeMap : drawableTypeMaps) {
            for (Object drawableTypeEntryObj : drawableTypeMap.entrySet()) {
                try {
                    Map.Entry<String, Object> drawableTypeEntry = (Map.Entry<String, Object>) drawableTypeEntryObj;
                    parseDrawableTypeEntry(drawableTypeEntry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void parseDrawableTypeEntry(Map.Entry<String, Object> entry) throws Exception {
        // Get DrawableType
        String drawableTypeStr = entry.getKey();
        DrawableType drawableType = stringToDrawableType(entry.getKey());
        if (drawableType == null) {
            throw new Exception("Wrong/Missing DrawableType: " + drawableTypeStr);
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
        if (!keyToValueDrawableTypeMap.containsKey(key)) {
            keyToValueDrawableTypeMap.put(key, new HashMap<>());
        }
        if (values != null && values.size() > 0) {
            for (String value : values) {
                keyToValueDrawableTypeMap.get(key).put(value, drawableType);
            }
        } else {
            // Default case for when there's no values, only key.
            keyToValueDrawableTypeMap.get(key).put(null, drawableType);
        }
    }
}
