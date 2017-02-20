package com.github.rafasantos.ui;

public class MockConsoleUi extends ConsoleUi {
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
	
	public void flush() {
		output = new StringBuffer();
	}
	
}