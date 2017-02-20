package com.github.rafasantos.ui;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.RED;

import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

/**
 * User interface based on console.
 *  
 * @author rafael.santos.bra@gmail.com
 */
public class ConsoleUi {
	
	public void finalizeUi() {
		AnsiConsole.systemUninstall();
	}
	
	public void initUi() {
		AnsiConsole.systemInstall();
	}
	
	public void println(String s) {
		Printer.println(s);
	}
	
	public void printGreen(String s) {
		if (s != null) {
			Printer.println(ansi().fg(Color.GREEN).a(s).reset());
		}
	}
	
	public void printRed(String s) {
		if (s != null) {
			Printer.println(ansi().fg(RED).a(s).reset());
		}
	}
	
	public void printYellow(String s) {
		if (s != null) {
			Printer.println(ansi().fg(Color.YELLOW).a(s).reset());
		}
	}
	
}
