package bfst19.danmarkskort.utils;

import bfst19.danmarkskort.model.Drawable;
import bfst19.danmarkskort.model.WayType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class EnumHelper {
    /**
     * Convert a string to a WayType, returning null of it fails.
     * @param name The name of the enum.
     * @return The corresponding enum or null.
     */
    public static WayType stringToWayType(String name){
        try {
            return WayType.valueOf(name);
        } catch (IllegalArgumentException e){
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
}
