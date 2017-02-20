package com.github.rafasantos.cli;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.github.rafasantos.context.AppContext;
import com.github.rafasantos.controller.DiffController;
import com.github.rafasantos.exception.DiffFilterException;
import com.github.rafasantos.pojo.LinePojo;
import com.github.rafasantos.ui.ConsoleUi;

public class CliExecutor {

	private DiffController diffController;
	private ConsoleUi ui;
	
	/**
	 * Constructor which wires dependencies provided by {@code applicationContext}.
	 * @param applicationContext
	 */
	public CliExecutor(AppContext applicationContext) {
		 ui = applicationContext.getBean(ConsoleUi.class);
		 diffController = applicationContext.getBean(DiffController.class);
	}

	/**
	 * Executes the <i>difffilter</i> based on the command line options.
	 * @param cli containing the desired command line options
	 * @throws DiffFilterException
	 */
	public void execute(AppCliHandler cli) throws DiffFilterException {
		FileInputStream ffis = null;
		FileInputStream sfis = null;
		List<LinePojo> response = null;
		try {
			ffis = new FileInputStream(cli.getFirstFile());
			BufferedReader firstFileReader = new BufferedReader(new InputStreamReader(ffis));
			sfis = new FileInputStream(cli.getSecondFile());
			BufferedReader secondFileReader = new BufferedReader(new InputStreamReader(sfis));
			response = diffController.getDiff(
					firstFileReader,
					secondFileReader,
					cli.getUniqueIndexes(),
					cli.getTextDelimiter(),
					cli.getEqualsTemplate(),
					cli.getInsertTemplate(),
					cli.getUpdateTemplate(),
					cli.getDeleteTemplate());
		} catch (IOException e) {
			throw new DiffFilterException(e.getMessage());
		} finally {
			try {
				if (ffis != null) {
					ffis.close();
				}
				if (sfis != null) {
					sfis.close();
				}
			} catch (IOException e) {
				throw new DiffFilterException(e.getMessage());
			}
		}
		
		for(LinePojo l : response) {
			if (l.getDiffType() == LinePojo.DiffType.DELETED) {
				ui.printRed(l.getDiffText());
			} else if (l.getDiffType() == LinePojo.DiffType.UPDATED) {
				ui.printYellow(l.getDiffText());
			} else if (l.getDiffType() == LinePojo.DiffType.INSERTED) {
				ui.printGreen(l.getDiffText());
			} else {
				ui.println(l.getDiffText());
			}
		}
		
	}

}