package com.fbmadev.sepmerge;

import com.fbmadev.sepmerge.sepmerge_module.SepMerge;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "sepmerge", description = "Merge files files, implementing the 3-way merge algorithm.")
public class SepmergeApplication implements Runnable {

	public static void main(String[] args) {
		new CommandLine(new SepmergeApplication()).execute(args);
	}

	@Override
	public void run() {
		SepMerge.run(null, null, null, null);
		// Path currDirr = Paths.get(System.getProperty("user.dir"));
	}

}
