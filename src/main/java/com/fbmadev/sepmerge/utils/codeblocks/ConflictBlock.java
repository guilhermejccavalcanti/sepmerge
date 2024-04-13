package com.fbmadev.sepmerge.utils.codeblocks;

import java.util.ArrayList;
import java.util.List;

public class ConflictBlock extends CodeBlock {

    private List<String> left;
    private List<String> base;
    private List<String> right;

    private int leftStartIndex;
    private int baseStartIndex;
    private int rightStartIndex;

    private String leftFile = "";
    private String baseFile = "";
    private String rightFile = "";

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
                leftFile = line.substring(8);
            } else if (line.startsWith("|||||||")) {
                baseStartIndex = i;
                baseFile = line.substring(8);
            } else if (line.startsWith("=======")) {
                rightStartIndex = i;
            } else if (line.startsWith(">>>>>>>")) {
                rightFile = line.substring(8);
            }
        }
    }

    private void splitLines() {
        left = lines.subList(leftStartIndex + 1, baseStartIndex);
        base = lines.subList(baseStartIndex + 1, rightStartIndex);
        right = lines.subList(rightStartIndex + 1, lines.size() - 1);
    }

    public List<String> getLeft() {
        return left;
    }

    public void setLeft(List<String> left) {
        this.left = left;
    }

    public List<String> getBase() {
        return base;
    }

    public void setBase(List<String> base) {
        this.base = base;
    }

    public List<String> getRight() {
        return right;
    }

    public void setRight(List<String> right) {
        this.right = right;
    }

    @Override
    public List<String> getLines() {
        List<String> result = new ArrayList<>();

        result.add("<<<<<<< " + leftFile);
        result.addAll(left);
        result.add("||||||| " + baseFile);
        result.addAll(base);
        result.add("=======");
        result.addAll(right);
        result.add(">>>>>>> " + rightFile);

        return result;

    }

    public void setLeftFile(String leftFile) {
        this.leftFile = leftFile;
    }

    public void setBaseFile(String baseFile) {
        this.baseFile = baseFile;
    }

    public void setRightFile(String rightFile) {
        this.rightFile = rightFile;
    }

    public List<String> getTwoWayConflictLines() {
        List<String> result = new ArrayList<>();

        result.add("<<<<<<< " + leftFile);
        result.addAll(left);
        result.add("=======");
        result.addAll(right);
        result.add(">>>>>>> " + rightFile);

        return result;
    }
}
