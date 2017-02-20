package com.github.rafasantos.cli;

import java.io.FileNotFoundException;
import java.net.URL;

import org.junit.Test;

public class AppCliHandlerIT {
	
	@Test(expected=FileNotFoundException.class)
	public void fileDoesNotExist_ff() throws FileNotFoundException {
		URL secondFile = this.getClass().getClassLoader().getResource("second-file.tsv");
		String[] arguments = {"-ff", "files/does/not/exist/path", "-sf", secondFile.getPath()};
		new AppCliHandler(arguments);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void fileDoesNotExist_sf() throws FileNotFoundException {
		URL firstFile = this.getClass().getClassLoader().getResource("first-file.tsv");
		String[] arguments = {"-ff", firstFile.getPath(), "-sf", "files/does/not/exist/path"};
		new AppCliHandler(arguments);
	}
	
}
