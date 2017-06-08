package com.github.rafasantos.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorReport {
	
	private static List<String> errors = new ArrayList<>();
	
	/**
	 * Add an error to the report
	 * @param errorMessage The java exception message or equivalent
	 * @param lineNumber The line number where the error happened
	 * @param originalLine The original line which caused the error
	 */
	public static void addError(String errorMessage, Long lineNumber, String originalLine) {
		String error = 
				"\nError message: " + errorMessage +
				"\nLine number: " + lineNumber +
				"\nOriginal line: " + originalLine;
		errors.add(error);
	}

	public static List<String> getErrors() {
		return errors;
	}

}
