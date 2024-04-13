package com.fbmadev.sepmerge.sepmerge_module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.fbmadev.sepmerge.diff3_module.Diff3Runner;
import com.fbmadev.sepmerge.utils.CodeBlocksReader;
import com.fbmadev.sepmerge.utils.codeblocks.CodeBlock;
import com.fbmadev.sepmerge.utils.codeblocks.ConflictBlock;

public class SepMerge {

    private static final Path tmpFolder = Path.of(System.getProperty("java.io.tmpdir"));

    public static void run(Path left, Path base, Path right, Path output) {
        System.err.println("Running SepMerge");
        System.err.println("tmpFolder: " + tmpFolder);

        Path diff3Tmp = tmpFolder.resolve("diff3.tmp");

        runDiff3(left, base, right, diff3Tmp);

        try {
            List<CodeBlock> codeBlocks = CodeBlocksReader.readCodeBlocks(Files.readAllLines(diff3Tmp));
            List<CodeBlock> resolvedCodeBlocks = processCodeBlocks(codeBlocks);
            writeOutput(resolvedCodeBlocks, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeOutput(List<CodeBlock> codeBlocks, Path output) throws IOException {
        StringJoiner sj = new StringJoiner("\n");
        for (CodeBlock codeBlock : codeBlocks) {
            sj.add(codeBlock.toString());
        }
        Files.writeString(output, sj.toString() + "\n");
    }

    private static List<CodeBlock> processCodeBlocks(List<CodeBlock> codeBlocks) {
        List<CodeBlock> resolvedCodeBlocks = new ArrayList<>();
        for (CodeBlock codeBlock : codeBlocks) {
            if (codeBlock instanceof ConflictBlock) {
                resolvedCodeBlocks.addAll(resolveConflict((ConflictBlock) codeBlock));
            } else {
                resolvedCodeBlocks.add(codeBlock);
            }
        }
        return resolvedCodeBlocks;
    }

    private static List<CodeBlock> resolveConflict(ConflictBlock conflictBlock) {
        // TODO: Implement conflict resolution
        return List.of(conflictBlock);
    }

    private static void runDiff3(Path left, Path base, Path right, Path output) {
        try {
            Diff3Runner.runDiff3(left.toString(), base.toString(), right.toString(), output.toString());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
