package bfst19.danmarkskort.utils;

import bfst19.danmarkskort.Main;

import java.io.InputStream;
import java.net.URL;

/***
 * We use this helper-class instead of the normal "getClass().getResource()" approach, since the
 * path is different depending on whether we're running the program from a .jar file or from Gradle.
 * For .jar files we need the leading slash, but that won't work if we use Gradle.
 */

public class ResourceLoader {
    private static String testFile = "DO_NOT_DELETE";
    private static Boolean useLeadingSlash = null;

    public static URL getResource(String name) {
        name = fixPath(name);
        return Main.class.getResource(name);
    }

    public static InputStream getResourceAsStream(String name) {
        name = fixPath(name);
        return Main.class.getResourceAsStream(name);
    }

    public static String fixPath(String path) {
        if (path == null) {
            return null;
        }
        if (getUseLeadingSlash()) {
            if (!path.startsWith("/")) {
                return "/" + path;
            }
        } else {
            if (path.startsWith("/")) {
                return path.substring(1);
            }
        }
        return path;
    }

    public static boolean getUseLeadingSlash() {
        if (useLeadingSlash == null) {
            useLeadingSlash = Main.class.getResource("/" + testFile) != null;
        }
        return useLeadingSlash;

    }
}
