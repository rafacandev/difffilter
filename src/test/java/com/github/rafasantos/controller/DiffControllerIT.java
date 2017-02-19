package com.github.rafasantos.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.rafasantos.context.ContextProvider;
import com.github.rafasantos.pojo.LinePojo;

public class DiffControllerIT {
	
	private DiffController fixture = ContextProvider.getDiffController();
	
	private static final String beforeInputString = 
			  "1	Alpha	one\n"
			+ "2	Beta	two\n"
			+ "3	Charlie	three\n"
			+ "10	Japan	ten\n"
			+ "5	Echo	five\n"
			+ "6	Foxtrot	six\n"
			+ "7	Gamma	seven\n"
			+ "9	India	nine"; 
	private static final String afterInputString =
			"1	Alpha	one\n"
			+ "2	Beta	two\n"
			+ "3	Charlie	three-updated\n"
			+ "4	Delta	four\n"
			+ "10	Japan	ten-updated\n"
			+ "6	Foxtrot	six\n"
			+ "8	Hotel	eight\n"
			+ "7	Gamma	seven";
	/*
	 * sqlTabDelimitedBefore compared to sqlTabDelimitedAfter:
	 * 
	 *  1. EQUALS
	 *  2. EQUALS
	 *  3. UPDATE
	 *  4. INSERT
	 *  5. DELETE
	 *  6. EQUALS
	 *  7. EQUALS
	 *  8. INSERT
	 *  9. DELETE
	 * 10. UPDATE
	 * 
	 */
	public static final String expectedOutputString =
			"-- 1	Alpha	one" + "\n"
			+ "-- 2	Beta	two" + "\n"
			+ "UPDATE user SET id='3', name='Charlie', description='three-updated';" + "\n"
			+ "INSERT INTO user (id, name, description) VALUES ('4', 'Delta', 'four');" + "\n"
			+ "DELETE FROM user WHERE id=5;" + "\n"
			+ "UPDATE user SET id='10', name='Japan', description='ten-updated';" + "\n"
			+ "-- 6	Foxtrot	six" + "\n"
			+ "INSERT INTO user (id, name, description) VALUES ('8', 'Hotel', 'eight');" + "\n"
			+ "DELETE FROM user WHERE id=9;" + "\n"
			+ "-- 7	Gamma	seven"  + "\n";

	@Test
	public void getDiff_positive() throws IOException {
		// Setup
		InputStream beforeInputStream = new ByteArrayInputStream(beforeInputString.getBytes());
		BufferedReader beforeReader = new BufferedReader(new InputStreamReader(beforeInputStream));
		InputStream afterInputStream = new ByteArrayInputStream(afterInputString.getBytes());
		BufferedReader afterReader = new BufferedReader(new InputStreamReader(afterInputStream));

		String insertTemplate = "INSERT INTO user (id, name, description) VALUES ('{0}', '{1}', '{2}');";
		String updateTemplate = "UPDATE user SET id='{0}', name='{1}', description='{2}';";
		String deleteTemplate = "DELETE FROM user WHERE id={0};";
		String equalsTemplate = "-- {ORIGINAL_LINE}";
		
		String primaryKeyIndexes = "0";
		
		// Run
		List<LinePojo> response= fixture.getDiff(beforeReader, afterReader, primaryKeyIndexes, "\t", equalsTemplate, insertTemplate, updateTemplate, deleteTemplate);
		
		StringBuffer result = new StringBuffer();
		for (LinePojo l : response) {
			result.append(l.getDiffText());
		}
		
		// Assert
		Assert.assertEquals(expectedOutputString, result.toString());
	}
	
}
