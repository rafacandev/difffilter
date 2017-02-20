package com.github.rafasantos.ui;

public class MockConsoleUi extends ConsoleUi {
	public StringBuffer output = new StringBuffer();
	@Override
	public void println(String s) {
		output.append(s + "\n");
	}
	@Override
	public void printGreen(String s) {
		println(s);
	}
	@Override
	public void printRed(String s) {
		println(s);
	}
	@Override
	public void printYellow(String s) {
		println(s);
	}
	
	public void flush() {
		output = new StringBuffer();
	}
	
}