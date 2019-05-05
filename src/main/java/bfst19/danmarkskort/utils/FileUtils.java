package bfst19.danmarkskort.utils;

import java.io.File;
import java.nio.file.Files;

public class FileUtils {
    public static void deleteDirectory(String path) {
        File file = new File(path);
        java.io.File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDirectory(f.getPath());
                }
            }
        }
        file.delete();
    }
}
