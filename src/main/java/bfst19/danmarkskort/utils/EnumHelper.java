package bfst19.danmarkskort.utils;

import bfst19.danmarkskort.model.drawables.Drawable;
import bfst19.danmarkskort.model.drawables.DrawableType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class EnumHelper {
    /**
     * Convert a string to a DrawableType, returning null of it fails.
     *
     * @param name The name of the enum.
     * @return The corresponding enum or null.
     */
    public static DrawableType stringToWayType(String name) {
        try {
            return DrawableType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Map<DrawableType, List<Drawable>> createWayTypeDrawablesMap() {
        java.util.Map<DrawableType, List<Drawable>> wayTypeEnumMap = new EnumMap<>(DrawableType.class);
        for (DrawableType type : DrawableType.values()) {
            wayTypeEnumMap.put(type, new ArrayList<>());
        }
        return wayTypeEnumMap;
    }

    public static String waytypeToDecoratedString(DrawableType drawableType) {
        String temp = drawableType.name();
        String[] words = temp.split("_");
        StringBuilder toReturn = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            StringBuilder stringBuilder = new StringBuilder(words[i].toLowerCase());
            stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
            toReturn.append(stringBuilder.toString());
            if (i < words.length - 1) {
                toReturn.append(" ");
            }
        }
        return toReturn.toString();
    }
}
