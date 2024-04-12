package com.fbmadev.sepmerge;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "sepmerge", description = "Merge files files, implementing the 3-way merge algorithm.")
public class SepmergeApplication implements Runnable {

	public static void main(String[] args) {
		new CommandLine(new SepmergeApplication()).execute(args);
	}

	@Override
	public void run() {
		System.out.println("Hello World!");
		// Path currDirr = Paths.get(System.getProperty("user.dir"));
	}

}
