package com.fbmadev.sepmerge.sepmerge_module;

import java.nio.file.Path;

public class SepMerge {

    private static final Path tmpFolder = Path.of(System.getProperty("java.io.tmpdir"));

    public static void run(Path left, Path base, Path right, Path output) {
        System.err.println("Running SepMerge");
        System.err.println("tmpFolder: " + tmpFolder);
    }
}
