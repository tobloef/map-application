package bfst19.osmdrawing.utils;

import bfst19.osmdrawing.model.WayType;
import bfst19.osmdrawing.model.DrawingInfo;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bfst19.osmdrawing.utils.EnumHelper.stringToWayType;

public class LoadWayTypeThemeMap {

    public static Map<WayType, DrawingInfo> loadWayTypeThemeMap(String path, Map<WayType, DrawingInfo> existingMap) throws YAMLException {
        Map<WayType, DrawingInfo> wayTypeThemeMap = existingMap;
        if (existingMap == null) {
            wayTypeThemeMap = new HashMap<>();
        }
        // Read the YAML file
        Yaml yaml = new Yaml();
        List<Map> themeMaps;
        InputStream inputStream = ResourceLoader.getResourceAsStream(path);
        themeMaps = yaml.load(inputStream);
        if (themeMaps == null) {
            return wayTypeThemeMap;
        }
        for (Map themeMap : themeMaps) {
            for (Object themeEntryObj : themeMap.entrySet()) {
                // Don't fail entire parsing if one entry fails.
                try {
                    Map.Entry<String, Object> themeEntry = (Map.Entry<String, Object>) themeEntryObj;
                    parseThemeEntry(themeEntry, wayTypeThemeMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Return the final theme map
        return wayTypeThemeMap;
    }

    private static void parseThemeEntry(Map.Entry<String, Object> themeEntry, Map<WayType, DrawingInfo> wayTypeThemeMap) throws Exception {
        String wayTypeStr = themeEntry.getKey();
        if (wayTypeStr.equals("constants")) {
            return;
        }
        WayType wayType = stringToWayType(wayTypeStr);
        if (wayType == null) {
            throw new Exception("Wrong/Missing WayType: " + wayTypeStr);
        }
        Map<String, Object> themeValuesMap = (Map<String, Object>) themeEntry.getValue();
        DrawingInfo theme = parseThemeValueMap(themeValuesMap);
        // Add WayType and theme to the map
        if (wayTypeThemeMap.containsKey(wayType)) {
            theme = wayTypeThemeMap.get(wayType).mergeWith(theme);
        }
        wayTypeThemeMap.put(wayType, theme);
    }

    private static DrawingInfo parseThemeValueMap(Map<String, Object> themeValuesMap) {
        Color fillColor = parseColor(themeValuesMap, "fillColor");
        Color strokeColor = parseColor(themeValuesMap, "strokeColor");
        Double lineDash = parseDouble(themeValuesMap, "lineDash");
        Double lineWidth = parseDouble(themeValuesMap, "lineWidth");
        Double zoomLevel = parseDouble(themeValuesMap, "zoomLevel");
        Boolean alwaysDraw = parseBoolean(themeValuesMap, "alwaysDraw");
        ImagePattern texture = parseTexture(themeValuesMap, "texture");
        // Create theme
        return new DrawingInfo(
                fillColor,
                strokeColor,
                lineDash,
                lineWidth,
                zoomLevel,
                alwaysDraw,
                texture
        );
    }

    private static ImagePattern parseTexture(Map<String, Object> themeValuesMap, String key) {
        String texturePath = (String) themeValuesMap.get(key);
        if (texturePath != null) {
            InputStream inputStream = ResourceLoader.getResourceAsStream(texturePath);
            return new ImagePattern(new Image(inputStream));
        }
        return null;
    }

    private static Double parseDouble(Map<String, Object> themeValuesMap, String key) {
        Object value = themeValuesMap.get(key);
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return Double.valueOf((Integer) value);
        }
        return null;
    }

    private static Boolean parseBoolean(Map<String, Object> themeValuesMap, String key) {
        Object value = themeValuesMap.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }

    private static Color parseColor(Map<String, Object> themeValuesMap, String key) {
        Object value = themeValuesMap.get(key);
        String fillColorHex = null;
        if (value instanceof String) {
            fillColorHex = (String) value;
        } else if (value instanceof Integer) {
            fillColorHex = ((Integer) value).toString();
        }
        if (fillColorHex != null) {
            return Color.web(fillColorHex);
        }
        return null;
    }
}