package bfst19.osmdrawing.utils;

import bfst19.osmdrawing.Main;

import java.io.InputStream;
import java.net.URL;

// We use this helper-class instead of the normal "getClass().getResource()" approach, since the
// path is different depending on whether we're running the program from a .jar file or from Gradle.
// For .jar files we need the leading slash, but that won't work if we use Gradle.
public class ResourceLoader {
    public static URL getResource(String name) {
        URL url = Main.class.getResource(name);
        if (url == null && name.startsWith("/")) {
            url = Main.class.getResource(name.substring(1));
        }
        else if (url == null && !name.startsWith("/")){
            url = Main.class.getResource("/" + name);
        }
        return url;
    }

    public static InputStream getResourceAsStream(String name) {
        InputStream stream = Main.class.getResourceAsStream(name);
        if (stream == null && name.startsWith("/")) {
            stream = Main.class.getResourceAsStream(name.substring(1));
        }
        else if (stream == null && !name.startsWith("/")){
            stream = Main.class.getResourceAsStream("/" + name);
        }
        return stream;
    }
}
