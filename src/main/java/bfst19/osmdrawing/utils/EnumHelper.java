package bfst19.osmdrawing.utils;

import bfst19.osmdrawing.model.WayType;

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
            e.printStackTrace();
        }
        return null;
    }
}
