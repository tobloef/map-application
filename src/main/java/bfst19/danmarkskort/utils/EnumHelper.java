package bfst19.danmarkskort.utils;

import bfst19.danmarkskort.model.drawables.Drawable;
import bfst19.danmarkskort.model.WayType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class EnumHelper {
    /**
     * Convert a string to a WayType, returning null of it fails.
     *
     * @param name The name of the enum.
     * @return The corresponding enum or null.
     */
    public static WayType stringToWayType(String name) {
        try {
            return WayType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Map<WayType, List<Drawable>> createWayTypeDrawablesMap() {
        java.util.Map<WayType, List<Drawable>> wayTypeEnumMap = new EnumMap<>(WayType.class);
        for (WayType type : WayType.values()) {
            wayTypeEnumMap.put(type, new ArrayList<>());
        }
        return wayTypeEnumMap;
    }

    public static String waytypeToDecoratedString(WayType wayType) {
        String temp = wayType.name();
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
