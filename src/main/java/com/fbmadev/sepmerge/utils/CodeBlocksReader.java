package com.fbmadev.sepmerge.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fbmadev.sepmerge.utils.codeblocks.CodeBlock;
import com.fbmadev.sepmerge.utils.codeblocks.ConflictBlock;
import com.fbmadev.sepmerge.utils.codeblocks.NormalBlock;

public class CodeBlocksReader {

    public static List<CodeBlock> readCodeBlocks(List<String> lines) throws IOException {
        List<CodeBlock> codeBlocks = new ArrayList<>();
        List<String> blockLines = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("<<<<<<<")) {
                if (!blockLines.isEmpty()) {
                    codeBlocks.add(new NormalBlock(blockLines));
                }
                blockLines = new ArrayList<>();
            }
            blockLines.add(line);
            if (line.startsWith(">>>>>>>")) {
                codeBlocks.add(new ConflictBlock(blockLines));
                blockLines = new ArrayList<>();
            }
        }
        if (!blockLines.isEmpty()) {
            codeBlocks.add(new NormalBlock(blockLines));
        }
        return codeBlocks;
    }

}
