package com.fbmadev.sepmerge.utils.codeblocks;

import java.util.List;

public abstract class CodeBlock {
    protected List<String> lines;

    public CodeBlock(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getLines() {
        return lines;
    }

    public String toString() {
        return String.join("\n", lines);
    }
}
