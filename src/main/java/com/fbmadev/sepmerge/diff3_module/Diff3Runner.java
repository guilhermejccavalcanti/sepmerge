package com.fbmadev.sepmerge.diff3_module;

import java.io.File;
import java.io.IOException;

public class Diff3Runner {

    public static void runDiff3(String left, String base, String right, String output)
            throws InterruptedException, IOException {
        System.err.println("Running Diff3");
        ProcessBuilder pb;
        pb = new ProcessBuilder("git", "merge-file", "-L", "MINE", "-L", "BASE", "-L", "YOURS", "-q", "-p",
                "--diff3", left, base, right).redirectOutput(new File(output));
//        if (System.getProperty("os.name").toLowerCase().contains("win")) {
//             pb = new ProcessBuilder("git", "merge-file", "-q", "-p", "--diff3", left, base, right).redirectOutput(new File(output));
//        } else {
//             pb = new ProcessBuilder("diff3", "-m", "-A", left, base, right).redirectOutput(new File(output));
//        }
        pb.start().waitFor();
    }
}
