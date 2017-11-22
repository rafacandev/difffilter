package com.github.rafasantos.cli;

import java.io.FileNotFoundException;
import java.net.URL;
import org.junit.Test;
import com.github.rafasantos.exception.DiffFilterException;
import static org.junit.Assert.assertEquals;

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

	@Test
	public void defaultCommandLineMustPickFistAndSecondArgumentsAsFirstAndSecondFile() throws FileNotFoundException, DiffFilterException {
		URL firstFile = this.getClass().getClassLoader().getResource("first-file.tsv");
		URL secondFile = this.getClass().getClassLoader().getResource("second-file.tsv");

		// Simple case: only first and second files
		String[] twoRegularArguments = {"-ff", firstFile.getPath(), "-sf", secondFile.getPath()};
		AppCliHandler twoRegularHandler = new AppCliHandler(twoRegularArguments);
		String[] twoShortArguments = {firstFile.getPath(), secondFile.getPath()};
		AppCliHandler twoShortHandler = new AppCliHandler(twoShortArguments);
		assertEquals(twoRegularHandler.getFirstFile(), twoShortHandler.getFirstFile());
		assertEquals(twoRegularHandler.getSecondFile(), twoShortHandler.getSecondFile());

		// Complex case: only first and second files
		String[] fiveRegularArguments = {"-ff", firstFile.getPath(), "-sf", secondFile.getPath(), "-ui", "0,1", "-it", "InsertTemplate", "-dt", "DeleteTemplate"};
		AppCliHandler fiveRegularHandler = new AppCliHandler(fiveRegularArguments);
		String[] fiveShortArguments = {firstFile.getPath(), secondFile.getPath(), "-ui", "0,1", "-it", "InsertTemplate", "-dt", "DeleteTemplate"};
		AppCliHandler fiveShortHandler = new AppCliHandler(fiveShortArguments);
		assertEquals(fiveRegularHandler.getFirstFile(), fiveShortHandler.getFirstFile());
		assertEquals(fiveRegularHandler.getSecondFile(), fiveShortHandler.getSecondFile());
		assertEquals(fiveRegularHandler.getUniqueIndexes(), fiveShortHandler.getUniqueIndexes());
		assertEquals(fiveRegularHandler.getInsertTemplate(), fiveShortHandler.getInsertTemplate());
		assertEquals(fiveRegularHandler.getDeleteTemplate(), fiveShortHandler.getDeleteTemplate());
	}

}
