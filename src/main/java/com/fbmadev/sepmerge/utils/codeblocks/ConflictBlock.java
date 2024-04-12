package com.fbmadev.sepmerge.utils.codeblocks;

import java.util.List;

public class ConflictBlock extends CodeBlock {

    private List<String> left;
    private List<String> base;
    private List<String> right;

    private int leftStartIndex;
    private int baseStartIndex;
    private int rightStartIndex;

    public ConflictBlock(List<String> lines) {
        super(lines);
        if (lines.size() < 3) {
            throw new IllegalArgumentException("Conflict block must have at least 3 lines");
        }
        if (!lines.get(0).startsWith("<<<<<<<")) {
            throw new IllegalArgumentException("Conflict block must start with '<<<<<<<', '|||||||', '>>>>>>>'");
        }
        findStartIndexes();
        splitLines();
    }

    private void findStartIndexes() {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("<<<<<<<")) {
                leftStartIndex = i;
            } else if (line.startsWith("|||||||")) {
                baseStartIndex = i;
            } else if (line.startsWith("=======")) {
                rightStartIndex = i;
            }
        }
    }

    private void splitLines() {
        left = lines.subList(leftStartIndex + 1, baseStartIndex);
        base = lines.subList(baseStartIndex + 1, rightStartIndex);
        right = lines.subList(rightStartIndex + 1, lines.size());
    }

    public List<String> getLeft() {
        return left;
    }

    public List<String> getBase() {
        return base;
    }

    public List<String> getRight() {
        return right;
    }
}
