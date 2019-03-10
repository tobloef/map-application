package bfst19.osmdrawing.utils;

import bfst19.osmdrawing.Main;

import java.io.InputStream;
import java.net.URL;

// We use this helper-class instead of the normal "getClass().getResource()" approach, since the
// path is different depending on whether we're running the program from a .jar file or from Gradle.
// For .jar files we need the leading slash, but that won't work if we use Gradle.
public class ResourceLoader {
    public static URL getResource(String name) {
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        URL url = Main.class.getResource(name);
        if (url == null) {
            url = Main.class.getResource("/" + name);
        }
        return url;
    }

    public static InputStream getResourceAsStream(String name) {
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        InputStream stream = Main.class.getResourceAsStream(name);
        if (stream == null) {
            stream = Main.class.getResourceAsStream("/" + name);
        }
        return stream;
    }
}
