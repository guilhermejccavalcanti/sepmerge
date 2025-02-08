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
        final String LEFT_FILE_PATH = "testcase/left.java";
        final String BASE_FILE_PATH = "testcase/base.java";
        final String RIGHT_FILE_PATH = "testcase/right.java";
        final String OUTPUT_FILE_PATH =  "testcase/output.java";

        final List<String> SEPARATORS = List.of("{", "}", ",", "(", ")", ";");

        // Run the java file merge operation
        SepMerge.run(Paths.get(LEFT_FILE_PATH), Paths.get(BASE_FILE_PATH), Paths.get(RIGHT_FILE_PATH),
                Paths.get(OUTPUT_FILE_PATH), SEPARATORS, LEFT_FILE_PATH, BASE_FILE_PATH, RIGHT_FILE_PATH);

        // Read and validate the merge result
        String mergeResult = readFileContent( Paths.get(OUTPUT_FILE_PATH));
        assertTrue(mergeResult.contains("<<<<<<<"));
    }

    @Test
    public void testMergingJavaFilesOnScreen() throws IOException {
        final String LEFT_FILE_PATH = "testcase/left.java";
        final String BASE_FILE_PATH = "testcase/base.java";
        final String RIGHT_FILE_PATH = "testcase/right.java";

        // Run the java file merge operation
        String mergeResult = SepMerge.run(LEFT_FILE_PATH, BASE_FILE_PATH,RIGHT_FILE_PATH);
        assertTrue(mergeResult.contains("<<<<<<<"));
    }

    // Helper method for reading file lines and joining them into a single string
    private String readFileContent(Path filePath) throws IOException {
        return Files.readAllLines(filePath).stream().collect(Collectors.joining());
    }
}
