package com.fbmadev.sepmerge.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fbmadev.sepmerge.utils.codeblocks.CodeBlock;
import com.fbmadev.sepmerge.utils.codeblocks.ConflictBlock;
import com.fbmadev.sepmerge.utils.codeblocks.NormalBlock;

import static com.fbmadev.sepmerge.constants.CodeBlockConstants.SAMPLE_CONFLICT_BLOCK;
import static com.fbmadev.sepmerge.constants.CodeBlockConstants.SAMPLE_NORMAL_BLOCK;

@SpringBootTest(classes = CodeBlocksReader.class)
public class CodeBlockReaderTests {

    @Test
    void readCodeBlocksTestWithEmptyLines() {
        List<String> lines = new ArrayList<>();
        try {
            List<CodeBlock> result = CodeBlocksReader.readCodeBlocks(lines);
            assert result.size() == 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readCodeBlocksTestWithNormalBlock() {
        List<String> normalBlock = SAMPLE_NORMAL_BLOCK;
        List<String> lines = new ArrayList<>();
        lines.addAll(normalBlock);
        try {
            List<CodeBlock> result = CodeBlocksReader.readCodeBlocks(lines);
            assert result.size() == 1;
            assert result.get(0) instanceof NormalBlock;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readCodeBlocksTestWithConflictBlock() {
        List<String> conflictBlock = SAMPLE_CONFLICT_BLOCK;
        List<String> lines = new ArrayList<>();
        lines.addAll(conflictBlock);
        try {
            List<CodeBlock> result = CodeBlocksReader.readCodeBlocks(lines);
            assert result.size() == 1;
            assert result.get(0) instanceof ConflictBlock;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readCodeBlocksTestWithConsecutiveConflictBlocks() {
        List<String> conflictBlock = SAMPLE_CONFLICT_BLOCK;
        List<String> lines = new ArrayList<>();
        lines.addAll(conflictBlock);
        lines.addAll(conflictBlock);
        try {
            List<CodeBlock> result = CodeBlocksReader.readCodeBlocks(lines);
            assert result.size() == 2;
            assert result.get(0) instanceof ConflictBlock;
            assert result.get(1) instanceof ConflictBlock;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readCodeBlocksTestWithConsecutiveNormalBlocks() {
        List<String> normalBlock = SAMPLE_NORMAL_BLOCK;
        List<String> lines = new ArrayList<>();
        lines.addAll(normalBlock);
        lines.addAll(normalBlock);
        try {
            List<CodeBlock> result = CodeBlocksReader.readCodeBlocks(lines);
            assert result.size() == 1;
            assert result.get(0) instanceof NormalBlock;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readCodeBlocksTestWithConsecutiveNormalAndConflictBlocks() {
        List<String> conflictBlock = SAMPLE_CONFLICT_BLOCK;
        List<String> normalBlock = SAMPLE_NORMAL_BLOCK;
        List<String> lines = new ArrayList<>();
        lines.addAll(normalBlock);
        lines.addAll(conflictBlock);
        lines.addAll(normalBlock);
        lines.addAll(conflictBlock);
        try {
            List<CodeBlock> result = CodeBlocksReader.readCodeBlocks(lines);
            assert result.size() == 4;
            assert result.get(0) instanceof NormalBlock;
            assert result.get(1) instanceof ConflictBlock;
            assert result.get(2) instanceof NormalBlock;
            assert result.get(3) instanceof ConflictBlock;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
