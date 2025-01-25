package com.fbmadev.sepmerge;

import com.fbmadev.sepmerge.sepmerge_module.SepMerge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

class SepmergeApplicationTests {

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void init() {
        File outputFile = new File("testcase/output.java");
        if (outputFile.exists()) {
            if (!outputFile.delete()) throw new RuntimeException("Failed to delete " +
                    "already existing output file");
        }
    }

    @Test
    public void testMergingJavaFiles() throws IOException {
        final Path LEFT_FILE_PATH = Paths.get("testcase/left.java");
        final Path BASE_FILE_PATH = Paths.get("testcase/right.java");
        final Path RIGHT_FILE_PATH = Paths.get("testcase/base.java");
        final Path OUTPUT_FILE_PATH = Paths.get("testcase/output.java");
        final List<String> SEPARATORS = List.of("{", "}", ",", "(", ")", ";");

        // Run the java file merge operation
        SepMerge.run(LEFT_FILE_PATH, BASE_FILE_PATH, RIGHT_FILE_PATH, OUTPUT_FILE_PATH, SEPARATORS, "", "", "");

        // Read and validate the merge result
        String mergeResult = readFileContent(OUTPUT_FILE_PATH);
        assertTrue(mergeResult.contains("<<<<<<<"));
    }

    // Helper method for reading file lines and joining them into a single string
    private String readFileContent(Path filePath) throws IOException {
        return Files.readAllLines(filePath).stream().collect(Collectors.joining());
    }
}
