package com.fbmadev.sepmerge.utils.codeblocks;

import java.util.List;

public abstract class CodeBlock {
    protected List<String> lines;

    public CodeBlock(List<String> lines) {
        this.lines = lines;
    }
}
