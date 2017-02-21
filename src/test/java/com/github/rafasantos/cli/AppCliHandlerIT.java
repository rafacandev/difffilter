package com.github.rafasantos.cli;

import java.io.FileNotFoundException;
import java.net.URL;

import org.junit.Test;

import com.github.rafasantos.exception.DiffFilterException;

public class AppCliHandlerIT {
	
	@Test(expected=FileNotFoundException.class)
	public void fileDoesNotExist_ff() throws FileNotFoundException, DiffFilterException {
		URL secondFile = this.getClass().getClassLoader().getResource("second-file.tsv");
		String[] arguments = {"-ff", "files/does/not/exist/path", "-sf", secondFile.getPath()};
		new AppCliHandler(arguments);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void fileDoesNotExist_sf() throws FileNotFoundException, DiffFilterException {
		URL firstFile = this.getClass().getClassLoader().getResource("first-file.tsv");
		String[] arguments = {"-ff", firstFile.getPath(), "-sf", "files/does/not/exist/path"};
		new AppCliHandler(arguments);
	}
	
	@Test
	public void validConstructor() throws FileNotFoundException, DiffFilterException {
		URL firstFile = this.getClass().getClassLoader().getResource("first-file.tsv");
		URL secondFile = this.getClass().getClassLoader().getResource("second-file.tsv");
		String[] arguments = {"-ff", firstFile.getPath(), "-sf", secondFile.getPath(), ""};
		new AppCliHandler(arguments);
	}
	
	@Test(expected=DiffFilterException.class)
	public void uniqueIndexes_negative() throws FileNotFoundException, DiffFilterException {
		URL firstFile = this.getClass().getClassLoader().getResource("first-file.tsv");
		URL secondFile = this.getClass().getClassLoader().getResource("second-file.tsv");
		String[] arguments = {"-ff", firstFile.getPath(), "-sf", secondFile.getPath(), "-ui", "invalid"};
		new AppCliHandler(arguments);
	}
	
	@Test
	public void uniqueIndexes_positive() throws FileNotFoundException, DiffFilterException {
		URL firstFile = this.getClass().getClassLoader().getResource("first-file.tsv");
		URL secondFile = this.getClass().getClassLoader().getResource("second-file.tsv");
		String[] a1 = {"-ff", firstFile.getPath(), "-sf", secondFile.getPath(), "-ui", "0"};
		new AppCliHandler(a1);
		String[] a2= {"-ff", firstFile.getPath(), "-sf", secondFile.getPath(), "-ui", "0,1"};
		new AppCliHandler(a2);
		String[] a3= {"-ff", firstFile.getPath(), "-sf", secondFile.getPath(), "-ui", "0,1,2"};
		new AppCliHandler(a3);
	}
	
}
