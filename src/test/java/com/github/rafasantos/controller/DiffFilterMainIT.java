package com.github.rafasantos.controller;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.github.rafasantos.DiffFilterMain;
import com.github.rafasantos.ui.ConsoleUi;

public class DiffFilterMainIT {
	
	private DiffFilterMain fixture = new DiffFilterMain();
	
	private class MockConsoleUi extends ConsoleUi {
		public StringBuffer output = new StringBuffer();
		@Override
		public void print(String s) {
			output.append(s);
		}
		@Override
		public void printGreen(String s) {
			print(s);
		}
		@Override
		public void printRed(String s) {
			print(s);
		}
		@Override
		public void printYellow(String s) {
			print(s);
		}
	}
	
	@Test
	public void helpInfo() {
		MockConsoleUi mockUi = new MockConsoleUi();
		DiffFilterMain.setUi(mockUi);
		String[] arguments = {"-h"};
		fixture.run(arguments);
		Assert.assertTrue(mockUi.output.toString().contains("--help"));
	}
	
	@Test
	public void diffMinimalistic() {
		URL firstFile = this.getClass().getClassLoader().getResource("first-file.tsv");
		URL secondFile = this.getClass().getClassLoader().getResource("second-file.tsv");
		String[] arguments = {"-ff", firstFile.getPath(), "-sf", secondFile.getPath()};
		fixture.run(arguments);
	}
	
}
