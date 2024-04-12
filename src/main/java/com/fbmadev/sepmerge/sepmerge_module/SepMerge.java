package com.fbmadev.sepmerge.sepmerge_module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fbmadev.sepmerge.diff3_module.Diff3Runner;
import com.fbmadev.sepmerge.utils.CodeBlocksReader;
import com.fbmadev.sepmerge.utils.codeblocks.CodeBlock;

public class SepMerge {

    private static final Path tmpFolder = Path.of(System.getProperty("java.io.tmpdir"));

    public static void run(Path left, Path base, Path right, Path output) {
        System.err.println("Running SepMerge");
        System.err.println("tmpFolder: " + tmpFolder);

        Path diff3Tmp = runDiff3(left, base, right);

        try {
            List<CodeBlock> codeBlocks = CodeBlocksReader.readCodeBlocks(Files.readAllLines(diff3Tmp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path runDiff3(Path left, Path base, Path right) {
        try {
            Path diff3Tmp = tmpFolder.resolve("diff3.tmp");
            Diff3Runner.runDiff3(left.toString(), base.toString(), right.toString(), diff3Tmp.toString());
            return diff3Tmp;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
