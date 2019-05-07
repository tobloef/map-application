package bfst19.danmarkskort.utils;

import bfst19.danmarkskort.model.drawables.DrawingInfo;
import bfst19.danmarkskort.model.drawables.Theme;
import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.model.drawables.NullWrapper;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static bfst19.danmarkskort.utils.EnumHelper.stringToWayType;

public class ThemeLoader {

    public static Theme loadTheme(String path) throws YAMLException {
        return loadTheme(path, null);
    }

    public static Theme loadTheme(String path, Theme existingTheme) throws YAMLException {
        Theme theme = existingTheme;
        if (theme == null) {
            theme = new Theme();
        }
        // Read the YAML file
        Yaml yaml = new Yaml();
        List<Map> themeMaps;
        InputStream inputStream = ResourceLoader.getResourceAsStream(path);
        themeMaps = yaml.load(inputStream);
        if (themeMaps == null) {
            return theme;
        }
        for (Map themeMap : themeMaps) {
            for (Object themeEntryObj : themeMap.entrySet()) {
                // Don't fail entire OSMparsing if one entry fails.
                try {
                    Map.Entry<String, Object> themeEntry = (Map.Entry<String, Object>) themeEntryObj;
                    parseThemeEntry(themeEntry, theme);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Return the final theme map
        return theme;
    }

    private static void parseThemeEntry(Map.Entry<String, Object> themeEntry, Theme theme) throws Exception {
        String wayTypeStr = themeEntry.getKey();
        if (wayTypeStr.equals("constants")) {
            return;
        }
        DrawableType drawableType = stringToWayType(wayTypeStr);
        if (drawableType == null) {
            throw new Exception("Wrong/Missing DrawableType: " + wayTypeStr);
        }
        Map<String, Object> themeValuesMap = (Map<String, Object>) themeEntry.getValue();
        DrawingInfo drawingInfo = parseThemeValueMap(themeValuesMap);
        // Add DrawableType and theme to the map
        theme.addDrawingInfo(drawableType, drawingInfo);
    }

    private static DrawingInfo parseThemeValueMap(Map<String, Object> themeValuesMap) {
        NullWrapper<Color> fillColor = null;
        NullWrapper<Color> strokeColor = null;
        NullWrapper<Double> lineDash = null;
        NullWrapper<Double> lineWidth = null;
        NullWrapper<Double> lineWidthMax = null;
        NullWrapper<Double> lineWidthMin = null;
        NullWrapper<Double> zoomLevel = null;
        NullWrapper<Boolean> alwaysDraw = null;
        NullWrapper<ImagePattern> texture = null;
        if (themeValuesMap.containsKey("fillColor")) {
            fillColor = new NullWrapper<>(parseColor(themeValuesMap, "fillColor"));
        }
        if (themeValuesMap.containsKey("strokeColor")) {
            strokeColor = new NullWrapper<>(parseColor(themeValuesMap, "strokeColor"));
        }
        if (themeValuesMap.containsKey("lineDash")) {
            lineDash = new NullWrapper<>(parseDouble(themeValuesMap, "lineDash"));
        }
        if (themeValuesMap.containsKey("lineWidth")) {
            lineWidth = new NullWrapper<>(parseDouble(themeValuesMap, "lineWidth"));
        }
        if (themeValuesMap.containsKey("lineWidthMax")) {
            lineWidthMax = new NullWrapper<>(parseDouble(themeValuesMap, "lineWidthMax"));
        }
        if (themeValuesMap.containsKey("lineWidthMin")) {
            lineWidthMin = new NullWrapper<>(parseDouble(themeValuesMap, "lineWidthMin"));
        }
        if (themeValuesMap.containsKey("zoomLevel")) {
            zoomLevel = new NullWrapper<>(parseDouble(themeValuesMap, "zoomLevel"));
        }
        if (themeValuesMap.containsKey("alwaysDraw")) {
            alwaysDraw = new NullWrapper<>(parseBoolean(themeValuesMap));
        }
        if (themeValuesMap.containsKey("texture")) {
            texture = new NullWrapper<>(parseTexture(themeValuesMap));
        }
        return new DrawingInfo(
                fillColor,
                strokeColor,
                lineDash,
                lineWidth,
                lineWidthMax,
                lineWidthMin,
                zoomLevel,
                alwaysDraw,
                texture
        );
    }

    private static ImagePattern parseTexture(Map<String, Object> themeValuesMap) {
        String texturePath = (String) themeValuesMap.get("texture");
        if (texturePath != null) {
            InputStream inputStream = ResourceLoader.getResourceAsStream(texturePath);
            return new ImagePattern(new Image(Objects.requireNonNull(inputStream)));
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

    private static Boolean parseBoolean(Map<String, Object> themeValuesMap) {
        Object value = themeValuesMap.get("alwaysDraw");
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