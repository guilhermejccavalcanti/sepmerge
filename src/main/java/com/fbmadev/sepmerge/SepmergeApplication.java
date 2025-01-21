package com.fbmadev.sepmerge;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.fbmadev.sepmerge.sepmerge_module.SepMerge;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "sepmerge", description = "Merge files files, implementing the 3-way merge algorithm.")
public class SepmergeApplication implements Runnable {

	@Parameters(index = "0")
	private String left;

	@Parameters(index = "1")
	private String base;

	@Parameters(index = "2")
	private String right;

	@Option(names = { "-o", "--output" }, description = "Output file path.")
	private String output;

	public static void main(String[] args) {
		new CommandLine(new SepmergeApplication()).execute(args);
	}

	@Override
	public void run() {
		Path currDir = Paths.get(System.getProperty("user.dir"));
		Path leftFile = currDir.resolve(this.left);
		Path baseFile = currDir.resolve(this.base);
		Path rightFile = currDir.resolve(this.right);
		Path outputFile = this.output != null ? currDir.resolve(this.output) : null;

		//List<String> separators = List.of(":", "(", ")", ",");
		List<String> separators = List.of("{","}",",","(", ")",";");

		SepMerge.run(leftFile, baseFile, rightFile, outputFile, separators, this.left, this.base, this.right);
	}

}
