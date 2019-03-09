package bfst19.osmdrawing;

import java.io.InputStream;
import java.net.URL;

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
