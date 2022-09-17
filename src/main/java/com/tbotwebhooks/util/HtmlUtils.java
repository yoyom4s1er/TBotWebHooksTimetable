package com.tbotwebhooks.util;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Log4j2
public class HtmlUtils {

    public static Optional<String> getHtmlFileAsString(Path path) {

        if (Files.exists(path)) {
            try {
                return Optional.of(Files.readString(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public static Optional<String> getHtmlFileAsString(String path) {

        if (Files.exists(PathConverter.StringToPath(path))) {
            try {
                return Optional.of(Files.readString(PathConverter.StringToPath(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}


