package com.rest.api.sample.helper;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonReader {
        public static String read(String file) throws Exception {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + file)));
        }
}
