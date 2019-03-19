package bfst19.osmdrawing.model;

import bfst19.osmdrawing.utils.ResourceLoader;
import bfst19.osmdrawing.view.WayType;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class WayTypeFactory {
	private static final String wayTypesConfigPath = "config/wayTypes.yaml";

	private static Map<String, Map<String, WayType>> keyToValueWayTypeMap;

	public static WayType getType(String key, String value) {
		if (keyToValueWayTypeMap == null) {
			initializeWayTypes();
		}
		if (keyToValueWayTypeMap.containsKey(key)) {
			Map<String, WayType> values = keyToValueWayTypeMap.get(key);
			if (values.containsKey(value)) {
				return values.get(value);
			}
		}
		return null;
	}

	private static WayType stringToWayType(String name){
		try {
			WayType type = WayType.valueOf(name);
			return type;
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		// If it does exist then we return null (Which is what java should do anyway...)
		return null;
	}

	private static void initializeWayTypes() {
		keyToValueWayTypeMap = new HashMap<>();
		// Read the YAML file
		Yaml yaml = new Yaml();
		InputStream inputStream = ResourceLoader.getResourceAsStream(wayTypesConfigPath);
		List<Map> wayTypeMaps = yaml.load(inputStream);

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
		// Get WayType
		String wayTypeStr = entry.getKey();
		WayType wayType = stringToWayType(entry.getKey());
		if (wayType == null) {
			throw new Exception("Wrong/Missing WayType: " + wayTypeStr);
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
			addValues(wayType, key, values);
		}
	}

	private static void addValues(WayType wayType, String key, List<String> values) {
		if (!keyToValueWayTypeMap.containsKey(key)) {
			keyToValueWayTypeMap.put(key, new HashMap<>());
		}
		if (values != null && values.size() > 0) {
			for (String value : values) {
				keyToValueWayTypeMap.get(key).put(value, wayType);
			}
		} else {
			// Default case for when there's no values, only key.
			keyToValueWayTypeMap.get(key).put(null, wayType);
		}
	}

	private static boolean lineIsComment(String line) {
		return (line.trim().charAt(0) == '#');
	}
}
