package com.github.rafasantos.controller;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.github.rafasantos.DiffFilterMain;
import com.github.rafasantos.context.AppContextTesting;
import com.github.rafasantos.ui.ConsoleUi;
import com.github.rafasantos.ui.MockConsoleUi;

public class DiffFilterMainIT {
	
	private AppContextTesting ac = new AppContextTesting();
	private DiffFilterMain fixture = new DiffFilterMain(ac);
	
	@Test
	public void helpInfo() {
		String[] arguments = {"-h"};
		fixture.run(arguments, ac);
		MockConsoleUi ui = (MockConsoleUi) ac.getBean(ConsoleUi.class);
		Assert.assertTrue(ui.output.toString().contains("--help"));
	}
	
	@Test
	public void diffMinimalistic() {
		URL firstFile = this.getClass().getClassLoader().getResource("first-file.tsv");
		URL secondFile = this.getClass().getClassLoader().getResource("second-file.tsv");
		String[] arguments = {"-ff", firstFile.getPath(), "-sf", secondFile.getPath()};
		fixture.run(arguments, ac);

		String expected = 
				 "= 1	Alpha	one" + "\n"
				+"= 2	Beta	two" + "\n"
				+"- 3	Charlie	three" + "\n"
				+"+ 3	Charlie-updated	three" + "\n"
				+"- 10	Japan	ten" + "\n"
				+"+ 4	Delta	four" + "\n"
				+"- 5	Echo	five" + "\n"
				+"+ 10	Japan	ten-updated" + "\n"
				+"= 6	Foxtrot	six" + "\n"
				+"+ 8	Hotel	eight" + "\n"
				+"- 9	India	nine " + "\n"
				+"= 7	Gamma	seven" + "\n";
		
		MockConsoleUi ui = (MockConsoleUi) ac.getBean(ConsoleUi.class);
		Assert.assertEquals(expected, ui.output.toString());
	}
	
}
