difffilter
==========

Compares two files and display differences allowing custom templates.

Usage
-----

The minimalist usage will display the differences between two files.
```
java -jar -difffilter.jar -firstFile first-file.tsv --secondFile second-file.tsv
```

```
### first-file.tsv ###
1	Alpha	one
2	Beta	two
3	Charlie	three
10	Japan	ten
5	Echo	five
6	Foxtrot	six
7	Gamma	seven
9	India	nine 
```

```
### second-file.tsv ###
1	Alpha	one
2	Beta	two
3	Charlie-updated	three
4	Delta	four
10	Japan	ten-updated
6	Foxtrot	six
8	Hotel	eight
7	Gamma	seven
```

Differences between `first-file.tsv` and `second-file.tsv`. By default, lines will start with indicators to distinguish the kind of difference: equals `=`, delete `-`, insert `+`, update `!`.
```
= 1	Alpha	one
= 2	Beta	two
- 3	Charlie	three
+ 3	Charlie-updated	three
- 10	Japan	ten
+ 4	Delta	four
- 5	Echo	five
+ 10	Japan	ten-updated
= 6	Foxtrot	six
+ 8	Hotel	eight
- 9	India	nine 
= 7	Gamma	seven
```

### Unique Indexes
`--uniqueIndexes` will change how the application determine if lines are different.

For example, the command line below uses the index zero to determine uniqueness. The output differs from the minimalistic example. On the minimalistic example the uniqueness is determined by the entire line (default behaviour also achievable with the special flag {ORIGINAL_LINE}). On this example, the application will split each line (using the TAB character by default) and use the first column (indexes start at position zero) to determine uniqueness.

```
java -jar -difffilter.jar -firstFile first-file.tsv --secondFile second-file.tsv -ui 0
```
```
= 1	Alpha	one
= 2	Beta	two
! 3	Charlie-updated	three
+ 4	Delta	four
- 5	Echo	five
! 10	Japan	ten-updated
= 6	Foxtrot	six
+ 8	Hotel	eight
- 9	India	nine 
= 7	Gamma	seven
```

It is possible to pass multiple indexes separeted by comma when using `--ui`. On this example, the uniqueness is determined by the first column (index 0) and the second column (index 1).

```
java -jar -difffilter.jar -firstFile first-file.tsv --secondFile second-file.tsv -ui 0,1
```
```
= 1	Alpha	one
= 2	Beta	two
- 3	Charlie	three
+ 3	Charlie-updated	three
+ 4	Delta	four
- 5	Echo	five
! 10	Japan	ten-updated
= 6	Foxtrot	six
+ 8	Hotel	eight
- 9	India	nine 
= 7	Gamma	seven
```

### Templates
Templates changes the output. The supported templates are: `--equalsTemplate`, `--insertTemplate`, `--updateTemplate`, `--deleteTemplate`.
Use string literals or indexes surrounded by curly brackets to replace by their indexes. The special flag {ORIGINAL_LINE} is replaced by the original text. The special flag {IGNORE_LINE} indicates that nothing should not be displayed.

Example:
```
java -jar difffilter.jar --firstFile src/test/resources/first-file.tsv --secondFile=src/test/resources/second-file.tsv -it "NEW RECORD: ID:{0}, NAME: {1}" -dt "DELETED: {ORIGINAL_LINE}"
```
```
= 1	Alpha	one
= 2	Beta	two
DELETED: 3	Charlie	three
NEW RECORD: ID:3, NAME: Charlie-updated
DELETED: 10	Japan	ten
NEW RECORD: ID:4, NAME: Delta
DELETED: 5	Echo	five
NEW RECORD: ID:10, NAME: Japan
= 6	Foxtrot	six
NEW RECORD: ID:8, NAME: Hotel
DELETED: 9	India	nine 
= 7	Gamma	seven
```

### Help
The help command line provides more information about the available options.

```
java -jar difffilter.jar -h
```

```
usage: java -jar <THIS_JAR.jar> -ff=<FIRST_INPUT_FILE> -sf=<SECOND_INPUT_FILE>

=== SUMMARY ===
Outputs the differences between two files (--firstFile and --secondFile).
Additionally, it is capable to apply templates and indicate the unique indexes used during the comparison.

=== TEMPLATES ===
Templates changes the output. The supported templates are: --equalsTemplate, --insertTemplate, --updateTemplate, --deleteTemplate. Use string literals or
numbers surrounded by curly brackets to replace by their indexes. The special flag {ORIGINAL_LINE} is replaced by the original text. The special flag
{IGNORE_LINE} indicates that nothing should not be displayed.Values between curly brackets will be replaced by their split indexes. The special flag
{ORIGINAL_LINE} is replaced by the original text. The special flag {IGNORE_LINE} indicates that lines should not be output.

=== EXAMPLES ===
Display the differences between first-file.tsv and second-file.tsv, split each line using the TAB character, use the first and second column (-ui 1) as unique
identifier, do not display the identical records and display 'NEW ROW: ' followed by the original line.
java -jar difffilter.jar -ff first-file.tsv -sf second-file.tsv -td "\t" -ui "0,1" -et {IGNORE_LINE} -it "NEW ROW: {ORIGINAL_LINE}"


   -h,--help                                Display help information.
                                            
   -ui,--uniqueIndexes <UNIQUE_INDEXES>     The indexes which creates a unique identified. Differently from regular 'diff' which compares files line by line,
                                            this application compares files base on unique identifiers. Indexes are the numerical positions (starting with 0)
                                            resulting from spliting a line with <TEXT_DELIMITER>. Default, it will assume the entire line is the unique
                                            identified.
                                            Example:
                                            Given <TEXT_DELIMITER> = ',' when the line '1,Robert,Smith' and '1,J,Smith'.
                                            If <UNIQUE_INDEXES> is 0 or 2 it will flag the two lines as 'updated'.
                                            If <UNIQUE_INDEXES> is 1 it will flag the two lines as 'inserted'.
   -dt,--deleteTemplate <DELETE_TEMPLATE>   Template used when a line is identified as 'deleted', hence it exist on the <FIRST_INPUT_FILE>; but does not exist
                                            on the <SECOND_INPUT_FILE>
   -ut,--updateTemplate <UPDATE_TEMPLATE>   Template used when a line is identified as 'updated', hence it exist on both the <FIRST_INPUT_FILE> and the
                                            <SECOND_INPUT_FILE>; but it's content is not identical.
   -ff,--firstFile <FIRST_INPUT_FILE>       Mandatory, the file path of the first file to be compared.
                                            
   -sf,--secondFile <SECOND_INPUT_FILE>     Mandatory, the file path of the second file to be compared.
                                            
   -td,--textDelimiter <TEXT_DELIMITER>     The text delimiter used in conjunction to templates (e.g.: insertTemplate, deleteTemplate...). The delimiter must be
                                            included when using templates with numeric index (e.g.: '{0}', etc). Otherwise unexpected results will occur. Most
                                            common delimiters are: tab '\t', pipe '|' and comma ',' and the default value is '\t'. The delimiters are matched
                                            against java regular expression.
   -et,--equalsTemplate <EQUALS_TEMPLATE>   Template used when a line is identified as 'equals' on the <FIRST_INPUT_FILE> and the <SECOND_INPUT_FILE>.
                                            
   -it,--insertTemplate <INSERT_TEMPLATE>   Template used when a line is identified as 'inserted', hence it does not exist on the <FIRST_INPUT_FILE>; but exists
                                            on the <SECOND_INPUT_FILE>.

https://github.com/rafasantos/difffilter
```