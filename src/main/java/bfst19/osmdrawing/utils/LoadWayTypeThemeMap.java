package bfst19.osmdrawing.utils;

import bfst19.osmdrawing.model.WayType;
import bfst19.osmdrawing.model.WayTypeTheme;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bfst19.osmdrawing.utils.EnumHelper.stringToWayType;

public class LoadWayTypeThemeMap {
    private static final String themePath = "config/themes/default.yaml";

    public static Map<WayType, WayTypeTheme> loadWayTypeThemeMap() {
        Map<WayType, WayTypeTheme> wayTypeThemeMap = new HashMap<>();
        // Read the YAML file
        Yaml yaml = new Yaml();
        InputStream inputStream = ResourceLoader.getResourceAsStream(themePath);
        List<Map> themeMaps = yaml.load(inputStream);
        for (Map themeMap : themeMaps) {
            for (Object themeEntryObj : themeMap.entrySet()) {
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

    private static void parseThemeEntry(Map.Entry<String, Object> themeEntry, Map<WayType, WayTypeTheme> wayTypeThemeMap) throws Exception {
        String wayTypeStr = themeEntry.getKey();
        WayType wayType = stringToWayType(wayTypeStr);
        if (wayType == null) {
            throw new Exception("Wrong/Missing WayType: " + wayTypeStr);
        }
        Map<String, Object> themeValuesMap = (Map<String, Object>) themeEntry.getValue();
        WayTypeTheme theme = parseThemeValueMap(themeValuesMap);
        // Add WayType and theme to the map
        wayTypeThemeMap.put(wayType, theme);
    }

    private static WayTypeTheme parseThemeValueMap(Map<String, Object> themeValuesMap) {
        Color fillColor = parseColor(themeValuesMap, "fillColor");
        Color strokeColor = parseColor(themeValuesMap, "strokeColor");
        double lineDash = parseDouble(themeValuesMap, "lineDash");
        double lineWidth = parseDouble(themeValuesMap, "lineWidth");
        ImagePattern texture = parseTexture(themeValuesMap, "texture");
        // Create theme
        return new WayTypeTheme(
                fillColor,
                strokeColor,
                lineDash,
                lineWidth,
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

    private static double parseDouble(Map<String, Object> themeValuesMap, String key) {
        Object value = themeValuesMap.get(key);
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return Double.valueOf((Integer) value);
        }
        return 0;
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