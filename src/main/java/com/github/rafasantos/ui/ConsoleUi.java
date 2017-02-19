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
	
	public static void finalizeUi() {
		AnsiConsole.systemUninstall();
	}
	
	public static void initUi() {
		AnsiConsole.systemInstall();
	}
	
	public static void print(String s) {
		Printer.print(s);
	}
	
	public static void printGreen(String s) {
		Printer.print(ansi().fg(Color.GREEN).a(s).reset());
	}
	
	public static void printRed(String s) {
		Printer.print(ansi().fg(RED).a(s).reset());
	}
	
	public static void printYellow(String s) {
		Printer.print(ansi().fg(Color.YELLOW).a(s).reset());
	}
	
}
