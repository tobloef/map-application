package bfst19.osmdrawing.utils;

import bfst19.osmdrawing.Main;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {
    public static URL getResource(String name) {
        URL url = Main.class.getResource(name);
        if (url == null && name.startsWith("/")) {
            url = Main.class.getResource(name.substring(1));
        }
        return url;
    }

    public static InputStream getResourceAsStream(String name) {
        InputStream stream = Main.class.getResourceAsStream("/" + name);
        if (stream == null && name.startsWith("/")) {
            stream = Main.class.getResourceAsStream(name.substring(1));
        }
        return stream;
    }
}
