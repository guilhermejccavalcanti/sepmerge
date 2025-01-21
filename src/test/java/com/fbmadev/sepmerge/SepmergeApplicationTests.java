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
			if(outputFile.exists()) {
				if(!outputFile.delete()) throw new RuntimeException("Failed to delete " +
						"already existing output file");
			}
		}

        @Test
		public void testMergingJavaFiles() throws IOException {
			String leftFileString = "";
			String baseFileString = "";
			String rightFileString = "";

			Path leftFilePath = Paths.get("testcase/left.java");
			Path baseFilePath = Paths.get("testcase/right.java");
			Path rightFilePath = Paths.get("testcase/base.java");
			Path outputFilePath = Paths.get("testcase/output.java");

			List<String> separators = List.of("{","}",",","(", ")",";");
			SepMerge.run(leftFilePath, baseFilePath, rightFilePath, outputFilePath, separators,
					leftFileString, baseFileString, rightFileString);

			String mergeResult = "";
			mergeResult = (Files.readAllLines(outputFilePath)).stream()
								.collect(Collectors.joining());

			assertTrue(mergeResult.contains("<<<<<<<"));
		}
	}
