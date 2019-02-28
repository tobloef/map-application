package bfst19.osmdrawing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class WayTypeFactory {
	private static Map<String, Map<String, WayType>> keyValuesToType;


	private static void addValues(WayType type, String key, List<String> values){
		addKeyIfNotPresent(key);
		if (values.size() != 0){
			for (String s : values){
				keyValuesToType.get(key).put(s, type);
			}
		}
		else {
			addNullKey(type, key);
		}
	}

	private static void addNullKey(WayType type, String key) {
		addKeyIfNotPresent(key);
		keyValuesToType.get(key).put(null, type);
	}

	private static void addKeyIfNotPresent(String key) {
		if (!keyValuesToType.containsKey(key)) {
			keyValuesToType.put(key, new HashMap<>());
		}
	}

	public static WayType getType(String key, String value){
		if (keyValuesToType == null) initializeWayTypes("wayTypes.txt");
		if (keyValuesToType.containsKey(key)){
			Map<String, WayType> keys = keyValuesToType.get(key);
			if (keys.containsKey(null)){
				return keys.get(null);
			}
			else if (keys.containsKey(value)){
				return keys.get(value);
			}
		}
		return null;
	}

	private static void initializeWayTypes(String filename) {
		try {
			keyValuesToType = new HashMap<>();
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				String first = st.nextToken();
				if (first.contains("#")) continue;
				WayType currentType = WayType.valueOf(first);
				String key = st.nextToken().trim();
				List<String> values = new ArrayList<>();
				while (st.hasMoreElements()) {
					values.add(st.nextToken().trim());
				}
				addValues(currentType, key, values);
			}
		}
		catch (Exception e){
			//Uncle bob is gonna find us and kill us all
		}
	}
}
