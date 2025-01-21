package com.fbmadev.sepmerge.csdiff_module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringJoiner;

import com.fbmadev.sepmerge.diff3_module.Diff3Runner;
import com.fbmadev.sepmerge.utils.CodeBlocksReader;
import com.fbmadev.sepmerge.utils.FileUtils;
import com.fbmadev.sepmerge.utils.codeblocks.CodeBlock;
import com.fbmadev.sepmerge.utils.codeblocks.ConflictBlock;
import com.fbmadev.sepmerge.utils.codeblocks.NormalBlock;

public class CSDiff {

    public static void runCSDiff(Path left, Path base, Path right, Path output, Path tmpPath, List<String> separators)
            throws IOException, InterruptedException {
        System.err.println("Running CSDiff");

        new File(tmpPath.toString()).mkdirs();

        Path leftTmpFile = tmpPath.resolve("left.tmp");
        Path baseTmpFile = tmpPath.resolve("base.tmp");
        Path rightTmpFile = tmpPath.resolve("right.tmp");

        addMarkers(left, leftTmpFile, separators);
        addMarkers(base, baseTmpFile, separators);
        addMarkers(right, rightTmpFile, separators);

        Path mergedTmp = tmpPath.resolve("merged.tmp");
        runDiff3(leftTmpFile, baseTmpFile, rightTmpFile, mergedTmp);

        List<CodeBlock> codeBlocks = CodeBlocksReader.readCodeBlocks(Files.readAllLines(mergedTmp));

        List<CodeBlock> resolvedCodeBlocks = codeBlocks.stream().map(codeBlock -> removeMarkers(codeBlock, separators))
                .toList();

        FileUtils.writeFile(output, resolvedCodeBlocks);

    }

    private static void addMarkers(Path input, Path output, List<String> separators) throws IOException {
        List<CodeBlock> codeBlocks = CodeBlocksReader.readCodeBlocks(Files.readAllLines(input));
        List<CodeBlock> markedCodeBlocks = codeBlocks.stream().map(codeBlock -> addMarkers(codeBlock, separators))
                .toList();
        FileUtils.writeFile(output, markedCodeBlocks);
    }

    private static CodeBlock addMarkers(CodeBlock codeBlock, List<String> separators) {
        List<String> lines = codeBlock.getLines();
        StringJoiner sj = new StringJoiner("\n");
        for (String line : lines) {
            for (String separator : separators) {
                line = line.replace(separator, String.format("%n%s%n", separator));
            }
            sj.add(line);
        }
        if (codeBlock instanceof ConflictBlock) {
            return new ConflictBlock(List.of(sj.toString().split("\n")));
        }
        return new NormalBlock(List.of(sj.toString().split("\n")));
    }

    private static CodeBlock removeMarkers(CodeBlock codeBlock, List<String> separators) {
        if (codeBlock instanceof ConflictBlock) {
            ConflictBlock conflictBlock = (ConflictBlock) codeBlock;
            CodeBlock left = removeMarkers(new NormalBlock(conflictBlock.getLeft()), separators);
            CodeBlock base = removeMarkers(new NormalBlock(conflictBlock.getBase()), separators);
            CodeBlock right = removeMarkers(new NormalBlock(conflictBlock.getRight()), separators);

            conflictBlock.setLeft(left.getLines());
            conflictBlock.setBase(base.getLines());
            conflictBlock.setRight(right.getLines());

            return new ConflictBlock(conflictBlock.getLines());
        } else {
            return removeMarkers((NormalBlock) codeBlock, separators);
        }
    }

    private static CodeBlock removeMarkers(NormalBlock normalBlock, List<String> separators) {
        List<String> lines = normalBlock.getLines();
        StringJoiner sj = new StringJoiner("\n");
        for (String line : lines) {
            sj.add(line);
        }
        sj.add("");
        String result = sj.toString();
        for (String separator : separators) {
            // Remove newline characters before and after the separator
            result = result.replace("\n" + separator + "\n", separator); // Newline before and after
        }
        return new NormalBlock(List.of(result.split("\n")));
    }

    private static void runDiff3(Path left, Path base, Path right, Path output) {
        try {
            Diff3Runner.runDiff3(left.toString(), base.toString(), right.toString(), output.toString());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
