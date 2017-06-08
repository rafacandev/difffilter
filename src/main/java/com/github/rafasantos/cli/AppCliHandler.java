package com.github.rafasantos.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.github.rafasantos.exception.DiffFilterException;
import com.github.rafasantos.util.AppConstants;

/**
 * Handles command line options including default options and capturing command line arguments.
 * @author rafael.santos.bra@gmail.com
 */
public class AppCliHandler {

	/**
	 * Command line options enumeration. Each options is composed by a {@code longText} and a {@code shortText</code.
	 * {@code longText} represents argument names prefixed by double dashes (e.g.: --help).
	 * {@code shorTest} represents argument names prefixed by single dashes (e.g.: -h).
	 * 
	 * @author rafael.santos.bra@gmail.com
	 */
	public enum CliOptions {
		FIRST_INPUT_FILE("firstFile", "ff"), 
		SECOND_INPUT_FILE("secondFile", "sf"),
		HELP("help", "h"),
		TEXT_DELIMITER("textDelimiter", "td"), 
		UNIQUE_INDEXES("uniqueIndexes", "ui"),
		EQUALS_TEMPLATE("equalsTemplate", "et"),
		INSERT_TEMPLATE("insertTemplate", "it"),
		UPDATE_TEMPLATE("updateTemplate", "ut"),
		DELETE_TEMPLATE("deleteTemplate", "dt"),
		NO_COLOR("noColors", "noColors");

		private final String longText;
		private final String shortText;
		private CliOptions(String longText, String shortText) {
			this.longText = longText;
			this.shortText = shortText;
		}
		public String getLongText() {
			return this.longText;
		}
		public String getShortText() {
			return this.shortText;
		}
	};
	
	private File firstFile;
	private File secondFile;
	private String textDelimiter = "\t";
	private String uniqueIndexes = "{ORIGINAL_LINE}";
	private String equalsTemplate = "= {ORIGINAL_LINE}";
	private String insertTemplate = "+ {ORIGINAL_LINE}";
	private String updateTemplate = "! {ORIGINAL_LINE}";
	private String deleteTemplate = "- {ORIGINAL_LINE}";
	private boolean isColoredOuput = true;
	private Options cliOptions;
	private boolean isHelp = false;

	/**
	 * Initialize this class with the provided {@code arguments}.
	 * @param arguments Command line arguments
	 * @throws FileNotFoundException
	 * @throws DiffFilterException 
	 */
	public AppCliHandler(String[] arguments) throws FileNotFoundException, DiffFilterException {
		cliOptions = new Options();
		cliOptions.addOption( Option.builder(CliOptions.FIRST_INPUT_FILE.getShortText())
				.longOpt(CliOptions.FIRST_INPUT_FILE.getLongText())
				.required(true)
				.hasArg(true)
				.argName(CliOptions.FIRST_INPUT_FILE.toString())
				.desc("Mandatory, the file path of the first file to be compared.\n")
				.build());
		
		cliOptions.addOption(Option.builder(CliOptions.SECOND_INPUT_FILE.getShortText())
				.longOpt(CliOptions.SECOND_INPUT_FILE.getLongText())
				.required(true)
				.hasArg(true)
				.argName(CliOptions.SECOND_INPUT_FILE.toString())
				.desc("Mandatory, the file path of the second file to be compared.\n")
				.build());

		cliOptions.addOption( Option.builder(CliOptions.TEXT_DELIMITER.getShortText())
				.longOpt(CliOptions.TEXT_DELIMITER.getLongText())
				.required(false)
				.hasArg(true)
				.argName(CliOptions.TEXT_DELIMITER.toString())
				.desc("The text delimiter used in conjunction to templates (e.g.: insertTemplate, deleteTemplate...). "
						+ "The delimiter must be included when using templates with numeric index (e.g.: '{0}', etc). "
						+ "Otherwise unexpected results will occur. "
						+ "Most common delimiters are: tab '\\t', pipe '|' and comma ',' and the default value is '\\t'. "
						+ "The delimiters are matched against java regular expression.\n")
				.build());

		cliOptions.addOption(Option.builder(CliOptions.EQUALS_TEMPLATE.getShortText())
				.longOpt(CliOptions.EQUALS_TEMPLATE.getLongText())
				.required(false)
				.hasArg(true)
				.argName(CliOptions.EQUALS_TEMPLATE.toString())
				.desc("Template used when a line is identified as 'equals' on the <"+CliOptions.FIRST_INPUT_FILE+"> and "
						+ "the <"+CliOptions.SECOND_INPUT_FILE+">.\n")
				.build());
		
		cliOptions.addOption(Option.builder(CliOptions.INSERT_TEMPLATE.getShortText())
				.longOpt(CliOptions.INSERT_TEMPLATE.getLongText())
				.required(false)
				.hasArg(true)
				.argName(CliOptions.INSERT_TEMPLATE.toString())
				.desc("Template used when a line is identified as 'inserted', hence it does not exist on the "
						+ "<"+CliOptions.FIRST_INPUT_FILE+">; but exists on the <"+CliOptions.SECOND_INPUT_FILE+">.\n")
				.build());
		
		cliOptions.addOption(Option.builder(CliOptions.UPDATE_TEMPLATE.getShortText())
				.longOpt(CliOptions.UPDATE_TEMPLATE.getLongText())
				.required(false)
				.hasArg(true)
				.argName(CliOptions.UPDATE_TEMPLATE.toString())
				.desc("Template used when a line is identified as 'updated', hence it exist on both the "
						+ "<"+CliOptions.FIRST_INPUT_FILE+"> and the <"+CliOptions.SECOND_INPUT_FILE+">; but it's content is not identical.\n")
				.build());

		cliOptions.addOption(Option.builder(CliOptions.DELETE_TEMPLATE.getShortText())
				.longOpt(CliOptions.DELETE_TEMPLATE.getLongText())
				.required(false)
				.hasArg(true)
				.argName(CliOptions.DELETE_TEMPLATE.toString())
				.desc("Template used when a line is identified as 'deleted', hence it exist on the <"+CliOptions.FIRST_INPUT_FILE+">; "
						+ "but does not exist on the <"+CliOptions.SECOND_INPUT_FILE+">\n")
				.build());

		cliOptions.addOption(Option.builder(CliOptions.NO_COLOR.getShortText())
				.longOpt(CliOptions.NO_COLOR.getLongText())
				.required(false)
				.hasArg(false)
				.argName(CliOptions.NO_COLOR.toString())
				.desc("Disable output colors. It will write in the default output color.\n")
				.build());
		
		cliOptions.addOption(Option.builder(CliOptions.UNIQUE_INDEXES.getShortText())
				.longOpt(CliOptions.UNIQUE_INDEXES.getLongText())
				.required(false)
				.hasArg(true)
				.argName(CliOptions.UNIQUE_INDEXES.toString())
				.desc("The indexes which creates a unique identified. Differently from regular 'diff' which compares files line by line, "
						+ "this application compares files base on unique identifiers. "
						+ "Indexes are the numerical positions (starting with 0) resulting from spliting a line with <"+ CliOptions.TEXT_DELIMITER +">. "
						+ "Default, it will assume the entire line is the unique identified."
						+ "\nExample:"
						+ "\nGiven <" + CliOptions.TEXT_DELIMITER +"> = ',' when the line '1,Robert,Smith' and '1,J,Smith'."
						+ "\n If <"+ CliOptions.UNIQUE_INDEXES +"> is 0 or 2 it will flag the two lines as 'updated'."
						+ "\n If <"+ CliOptions.UNIQUE_INDEXES +"> is 1 it will flag the two lines as 'inserted'.")
				.build());
		
		
		cliOptions.addOption( Option.builder(CliOptions.HELP.getShortText())
				.longOpt(CliOptions.HELP.getLongText())
				.required(false)
				.hasArg(false)
				.desc("Display help information.\n")
				.build());
		
		try {
			isHelp = handleHelp(arguments);
			if (!isHelp) {
				CommandLine cli = new DefaultParser().parse(cliOptions, arguments);
				readOptionValues(cli);
				validateOptions();
			}
		} catch (ParseException e) {
			throw new InvalidParameterException(e.getMessage());
		}
	}

	private void validateOptions() throws FileNotFoundException, DiffFilterException {
		if (!firstFile.exists() || !secondFile.exists()) {
			if (!this.firstFile.exists()) {
				throw new FileNotFoundException("First file does not exist: " + firstFile.getAbsolutePath());
			} else if (firstFile.isDirectory()) {
				throw new FileNotFoundException("The path provided is a directory instead of a valid file: " + firstFile.getAbsolutePath());
			} else if (!firstFile.isFile()) {
				throw new FileNotFoundException("The path provided is not a file: " + firstFile.getAbsolutePath());
			}
			if (!this.secondFile.exists()) {
				throw new FileNotFoundException("Second file does not exist: " + secondFile.getAbsolutePath());
			} else if (firstFile.isDirectory()) {
				throw new FileNotFoundException("The path provided is a directory instead of a valid file: " + secondFile.getAbsolutePath());
			} else if (!firstFile.isFile()) {
				throw new FileNotFoundException("The path provided is not a file: " + secondFile.getAbsolutePath());
			}
		}
		// uniqueIndexs must be {ORIGINAL_LINE} or contain numbers separated by comma
		if (!"{ORIGINAL_LINE}".equals(uniqueIndexes) && !uniqueIndexes.matches("^(\\d+(,\\d+)*)?$")) {
			throw new DiffFilterException("--uniqueIndexes option accepts only numbers separated by comma or the special flag {ORIGINAL_LINE}");
		}
	}

	private Comparator<Option> getDefaultHelpFormatterComparator() {
		return (Option o1, Option o2) -> {
			if (CliOptions.HELP.toString().equals(o1.getArgName())) {
				return 1000;
			} else if (CliOptions.FIRST_INPUT_FILE.toString().equals(o1.getArgName())) {
				return 100;
			} else if (CliOptions.SECOND_INPUT_FILE.toString().equals(o1.getArgName())) {
				return 90;
			} else if (CliOptions.TEXT_DELIMITER.toString().equals(o1.getArgName())) {
				return 80;
			} else if (CliOptions.EQUALS_TEMPLATE.toString().equals(o1.getArgName())) {
				return 70;
			} else if (CliOptions.INSERT_TEMPLATE.toString().equals(o1.getArgName())) {
				return 60;
			}
			return -1;
		};
	}

	public String getDeleteTemplate() {
		return deleteTemplate;
	}

	public String getEqualsTemplate() {
		return equalsTemplate;
	}

	public File getFirstFile() {
		return firstFile;
	}

	/**
	 * Return a string with the command line help
	 * @return help as string
	 */
	public String getHelpText() {
		
		
		String header = ""
				+ "=== SUMMARY ===\n"
				+ "Outputs the differences between two files (--firstFile and --secondFile).\n"
				+ "Additionally, it is capable to apply templates and indicate the unique indexes used during the comparison."
				+ "\n\n"
				+ "=== TEMPLATES ===\n"
				+ "Templates changes the output. The supported templates are: --equalsTemplate, --insertTemplate, --updateTemplate, --deleteTemplate. "
				+ "Use string literals or numbers surrounded by curly brackets to replace by their indexes. "
				+ "The special flag {ORIGINAL_LINE} is replaced by the original text. The special flag {IGNORE_LINE} indicates that nothing should not be displayed."
				+ "Values between curly brackets will be replaced by their split indexes. The special flag {"+AppConstants.ORIGINAL_LINE+"} is replaced by the original text. "
				+ "The special flag {"+AppConstants.IGNORE_LINE+"} indicates that lines should not be output."
				+ "\n\n"
				+ "=== EXAMPLES ===\n"
				+ "Display the differences between first-file.tsv and second-file.tsv, split each line using the TAB character, use the first and second column (-ui 1) as unique identifier, do not display the identical records and display 'NEW ROW: ' followed by the original line. "
				+ "\n"
				+ "java -jar difffilter.jar -ff first-file.tsv -sf second-file.tsv -td \"\\t\" -ui \"0,1\" -et {IGNORE_LINE} -it \"NEW ROW: {ORIGINAL_LINE}\""
				+ "\n\n\n";
		
		StringWriter stringWritter = new StringWriter();
		PrintWriter printWritter = new PrintWriter(stringWritter);
		HelpFormatter formatter = new HelpFormatter();
		formatter.setOptionComparator(getDefaultHelpFormatterComparator());
		formatter.printHelp(printWritter, 
				160, // Width
				"java -jar difffilter.jar -ff=<"+CliOptions.FIRST_INPUT_FILE+"> -sf=<"+CliOptions.SECOND_INPUT_FILE+"> \n\n", // Usage
				header,
				cliOptions, // Options
				3, // Left pad
				3, // Description pad
				"\nhttps://github.com/rafasantos/difffilter \n" // Footer
				);
		String commandLineOutputMessage = stringWritter.toString();
		return commandLineOutputMessage;
	}

	public String getInsertTemplate() {
		return insertTemplate;
	}

	public File getSecondFile() {
		return secondFile;
	}
	
	public String getTextDelimiter() {
		return textDelimiter;
	}

	public String getUniqueIndexes() {
		return uniqueIndexes;
	}
	
	public String getUpdateTemplate() {
		return updateTemplate;
	}
	
	private boolean handleHelp(String[] arguments) throws ParseException {
		Options helpOptions = new Options();
		Option helpOption = cliOptions.getOption(CliOptions.HELP.getShortText());
		helpOptions.addOption(helpOption);
		CommandLine helpCli = new DefaultParser().parse(helpOptions, arguments, true);
		return (helpCli.hasOption("h"));
	}

	/**
	 * If the output should support colors
	 * @return
	 */
	public boolean isColoredOuput() {
		return isColoredOuput;
	}

	/**
	 * If is a help command line (e.g.: -h)
	 * @return
	 */
	public boolean isHelp() {
		return this.isHelp;
	}

	private void readOptionValues(CommandLine cli) {
		this.textDelimiter = cli.getOptionValue(CliOptions.TEXT_DELIMITER.getShortText(), this.textDelimiter);
		this.uniqueIndexes = cli.getOptionValue(CliOptions.UNIQUE_INDEXES.getShortText(), this.uniqueIndexes);
		this.equalsTemplate = cli.getOptionValue(CliOptions.EQUALS_TEMPLATE.getShortText(), this.equalsTemplate);
		this.insertTemplate = cli.getOptionValue(CliOptions.INSERT_TEMPLATE.getShortText(), this.insertTemplate);
		this.updateTemplate = cli.getOptionValue(CliOptions.UPDATE_TEMPLATE.getShortText(), this.updateTemplate);
		this.deleteTemplate = cli.getOptionValue(CliOptions.DELETE_TEMPLATE.getShortText(), this.deleteTemplate);
		this.isColoredOuput = !cli.hasOption(CliOptions.NO_COLOR.getShortText());
		String firstFilePath = cli.getOptionValue(CliOptions.FIRST_INPUT_FILE.getShortText());
		this.firstFile = new File(firstFilePath);
		String secondFilePath = cli.getOptionValue(CliOptions.SECOND_INPUT_FILE.getShortText());
		this.secondFile = new File(secondFilePath);
	}
}