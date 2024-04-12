package com.fbmadev.sepmerge.constants;

import java.util.List;

public class CodeBlockConstants {

    public static final String START_LEFT_CONFLICT_BLOCK = "<<<<<<<";
    public static final String START_BASE_CONFLICT_BLOCK = "|||||||";
    public static final String START_RIGHT_CONFLICT_BLOCK = "=======";
    public static final String END_CONFLICT_BLOCK = ">>>>>>>";

    public static final List<String> SAMPLE_CONFLICT_BLOCK = List.of(START_LEFT_CONFLICT_BLOCK, "line", "line",
            START_BASE_CONFLICT_BLOCK, "line", "line", START_RIGHT_CONFLICT_BLOCK, "line", "line", END_CONFLICT_BLOCK);

    public static final List<String> SAMPLE_NORMAL_BLOCK = List.of("line", "line", "line");

}
