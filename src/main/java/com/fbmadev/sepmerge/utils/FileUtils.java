package com.fbmadev.sepmerge.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringJoiner;

import com.fbmadev.sepmerge.utils.codeblocks.CodeBlock;

public class FileUtils {

    public static void writeFile(Path file, List<CodeBlock> codeBlocks) throws IOException {
        StringJoiner sj = new StringJoiner("\n");
        for (CodeBlock codeBlock : codeBlocks) {
            sj.add(codeBlock.toString());
        }
        Files.writeString(file, sj.toString() + "\n");
    }

}
