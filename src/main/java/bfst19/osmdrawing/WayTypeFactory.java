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

	private static WayType getWayTypeFromString(String name){
		try {
			WayType type = WayType.valueOf(name);
			return type;
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			//If it does exist then we return null (Which is what java should do.
		}
		return null;
	}

	private static void initializeWayTypes(String filename) {
		try {
			keyValuesToType = new HashMap<>();
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				if (line.trim().isEmpty() || lineIsComment(line)){
					continue;
				}
				StringTokenizer st = new StringTokenizer(line);
				String first = st.nextToken();
				WayType currentType = getWayTypeFromString(first);
				if (currentType == null) {
					System.err.println("Wrong/Missing WayType: " + first);
					continue;
				}
				String key = st.nextToken().trim();
				List<String> values = new ArrayList<>();
				while (st.hasMoreElements()) {
					values.add(st.nextToken().trim());
				}
				addValues(currentType, key, values);
			}
		}
		catch (Exception e){
			e.printStackTrace();
			//Uncle bob is gonna find us and kill us all
		}
	}

	private static boolean lineIsComment(String line) {
		return (line.trim().charAt(0) == '#');
	}
}
