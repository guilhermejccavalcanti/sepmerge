package com.fbmadev.sepmerge.sepmerge_module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.fbmadev.sepmerge.SepmergeApplication;
import com.fbmadev.sepmerge.csdiff_module.CSDiff;
import com.fbmadev.sepmerge.diff3_module.Diff3Runner;
import com.fbmadev.sepmerge.utils.CodeBlocksReader;
import com.fbmadev.sepmerge.utils.FileUtils;
import com.fbmadev.sepmerge.utils.codeblocks.CodeBlock;
import com.fbmadev.sepmerge.utils.codeblocks.ConflictBlock;
import com.fbmadev.sepmerge.utils.codeblocks.NormalBlock;

public class SepMerge {

    private static final Path tmpFolder = Path.of(System.getProperty("java.io.tmpdir")
            + File.separator
            + String.valueOf(System.currentTimeMillis()));

    public static void run(Path left, Path base, Path right, Path output, List<String> separators,
            String leftFileString, String baseFileString, String rightFileString) {
        System.err.println("Running SepMerge");
        System.err.println("tmpFolder: " + tmpFolder);

        File tempFolderDir = new File(tmpFolder.toString());
        if(!tempFolderDir.exists()) tempFolderDir.mkdirs();

        Path diff3Tmp = tmpFolder.resolve("diff3.tmp");

        runDiff3(left, base, right, diff3Tmp);

        try {
            List<CodeBlock> codeBlocks = CodeBlocksReader.readCodeBlocks(Files.readAllLines(diff3Tmp));
            List<CodeBlock> resolvedCodeBlocks = processCodeBlocks(codeBlocks, separators);
            writeOutput(resolvedCodeBlocks, output, leftFileString, baseFileString, rightFileString);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String run(String leftFilePath, String baseFilePath, String rightFilePath) {
        System.err.println("Running SepMerge");
        System.err.println("tmpFolder: " + tmpFolder);

        File tempFolderDir = new File(tmpFolder.toString());
        if(!tempFolderDir.exists()) tempFolderDir.mkdirs();

        Path diff3Tmp = tmpFolder.resolve("diff3.tmp");

        runDiff3(Paths.get(leftFilePath), Paths.get(baseFilePath), Paths.get(rightFilePath), diff3Tmp);

        try {
            List<CodeBlock> codeBlocks = CodeBlocksReader.readCodeBlocks(Files.readAllLines(diff3Tmp));
            List<CodeBlock> resolvedCodeBlocks = processCodeBlocks(codeBlocks, SepmergeApplication.separators);
           return writeOutput(resolvedCodeBlocks, leftFilePath, baseFilePath, rightFilePath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeOutput(List<CodeBlock> codeBlocks, Path output, String leftFileString,
            String baseFileString, String rightFileString) throws IOException {
        StringJoiner sj = getStringJoiner(codeBlocks, leftFileString, baseFileString, rightFileString);
        Files.writeString(output, sj.toString() + "\n");
    }

    private static String writeOutput(List<CodeBlock> codeBlocks, String leftFileString,
                                    String baseFileString, String rightFileString) throws IOException {
        StringJoiner sj = getStringJoiner(codeBlocks, leftFileString, baseFileString, rightFileString);
        return (sj.toString() + "\n");
    }

    private static StringJoiner getStringJoiner(List<CodeBlock> codeBlocks, String leftFileString,
                                                String baseFileString, String rightFileString) {
        StringJoiner sj = new StringJoiner("\n");
        for (CodeBlock codeBlock : codeBlocks) {
            CodeBlock cb = codeBlock;
            if (codeBlock instanceof ConflictBlock) {
                ConflictBlock conflictBlock = (ConflictBlock) codeBlock;
                conflictBlock.setLeftFile(leftFileString);
                conflictBlock.setBaseFile(baseFileString);
                conflictBlock.setRightFile(rightFileString);
                cb = new NormalBlock(conflictBlock.getTwoWayConflictLines());
            }
            sj.add(cb.toString());
        }
        return sj;
    }

    private static List<CodeBlock> processCodeBlocks(List<CodeBlock> codeBlocks, List<String> separators)
            throws IOException, InterruptedException {
        List<CodeBlock> resolvedCodeBlocks = new ArrayList<>();
        for (CodeBlock codeBlock : codeBlocks) {
            if (codeBlock instanceof ConflictBlock) {
                resolvedCodeBlocks.addAll(resolveConflict((ConflictBlock) codeBlock, separators));
            } else {
                resolvedCodeBlocks.add(codeBlock);
            }
        }
        return resolvedCodeBlocks;
    }

    private static List<CodeBlock> resolveConflict(ConflictBlock conflictBlock, List<String> separators)
            throws IOException, InterruptedException {
        Path leftTmp = tmpFolder.resolve("left.tmp");
        Path baseTmp = tmpFolder.resolve("base.tmp");
        Path rightTmp = tmpFolder.resolve("right.tmp");

        FileUtils.writeFile(leftTmp, List.of(new NormalBlock(conflictBlock.getLeft())));
        FileUtils.writeFile(baseTmp, List.of(new NormalBlock(conflictBlock.getBase())));
        FileUtils.writeFile(rightTmp, List.of(new NormalBlock(conflictBlock.getRight())));

        Path mergedTmp = tmpFolder.resolve("csdiff.tmp");
        CSDiff.runCSDiff(leftTmp, baseTmp, rightTmp, mergedTmp, tmpFolder, separators);

        return CodeBlocksReader.readCodeBlocks(Files.readAllLines(mergedTmp));
    }

    private static void runDiff3(Path left, Path base, Path right, Path output) {
        try {
            Diff3Runner.runDiff3(left.toString(), base.toString(), right.toString(), output.toString());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
