package com.tbotwebhooks.util;

import javax.persistence.AttributeConverter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathConverter {

    public static String pathToString(Path path) {
        return path.toString();
    }

    public static Path StringToPath(String path) {
        return Paths.get(path);
    }
}
