package com.turnipcorp.Simpler.interpreter;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

import com.turnipcorp.Simpler.lang.InterpreterError;
import com.turnipcorp.Simpler.lang.SimplerException;

/**
 * The Simpler++ Interpreter with all its important methods, mostly private
 * <br /> List of attributes:
 * <ul>
 * <li> - integerMemory : HashMap[String, Integer]
 * <li> - stringMemory : HashMap[String, String]
 * <li> - mutableMemory : HashMap[String, Integer]
 * <li> - macroMemory : HashMap[String, String[]]
 * <li> - labelMemory : HashMap[String, Integer]
 * <li> - labelCallStack : HashMap[String, Integer]
 * <li> - consoleScanner : Scanner
 * <li> - tokenizedBySpace : String[]
 * <li> - tokenizedByQuotes : String[]
 * <li> - tokenizedByBraces : String[]
 * <li> - countLinesBeforeExecuting : String[]
 * <li> - endingStrings : String[]
 * <li> - localIntVariables : String[]
 * <li> - localStringVariables : String[]
 * <li> - macroCache : String[]
 * <li> - cacheType : String
 * <li> - cache : String[]
 * <li> - cacheCount : int
 * <li> - hasCache : boolean
 * <li> - indented : boolean
 * <li> - i : int [line number]
 * </ul>
 * <br /> List of methods:
 * <ul>
 * <li> + Interpreter ()
 * <li> + interpretAndRun (String[])  : void
 * <li> + interpretConsoleCode () : void
 * <li> + clearCache () : void
 * <li> + clearMemory () : void
 * <li> + toggleDebug () : void
 * <li> - tokenize (String) : String[]
 * <li> - tokenizeSimple (String) : String[]
 * <li> - tokenizeQuotes (String) : String[]
 * <li> - tokenizeBraces (String) : String[]
 * <li> - executeLine (String[]) : void
 * <li> - executeLine (String[], boolean) : void
 * <li> - addIntegerVariable (String[]) : void
 * <li> - addIntegerVariable (String[], boolean) : void
 * <li> - addMutableVariable (String[]) : void
 * <li> - changeMutableVariable (String[]) : void
 * <li> - changeIntegerVariable (String[]) : void
 * <li> - addStringVariable (String[]) : void
 * <li> - addStringVariable (String[], boolean) : void
 * <li> - changeStringVariable (String[]) : void
 * <li> - printVariable (String[]) : void
 * <li> - printVariable (String[], boolean) : void
 * <li> - doChoice (String[]) : void
 * <li> - addLabel (String[]) : void
 * <li> - jumptoLabel (String[]) : void
 * <li> - checkStack () : void
 * <li> - evaluate (String[]) : boolean
 * <li> - loop (String[]) : void
 * <li> - callMacro (String[]) : void
 * <li> - countLines (String[], int) : void
 * <li> - countMacroLines (String[], int) : void
 * <li> - printString (String[]) : void
 * </ul>
 * @see #Interpreter()
 * @see #interpretAndRun(String[])
 * @see #interpretConsoleCode()
 * @author Frank Dai
 * @version 1.3
 */
public class Interpreter {
	
	/**
	 * The memory reserved for user-created integer variables.
	 */
	private HashMap<String, Integer> integerMemory = new HashMap<>();
	
	/**
	 * The memory reserved for user-created string variables.
	 */
	private HashMap<String, String> stringMemory = new HashMap<>();
	
	/**
	 * The memory reserved for mutable integer variables.
	 */
	private HashMap<String, Integer> mutableMemory = new HashMap<>();
	
	/**
	 * Memory reserved for macros (functions)
	 */
	private HashMap<String, String[]> macroMemory = new HashMap<>();
	
	/**
	 * Memory reserved for GOTO labels and their line numbers.
	 */
	private HashMap<String, Integer> labelMemory = new HashMap<>();
	
	/**
	 * Counts the times each label has been jumped to, throws a StackOverflowError when any one of the values exceed 10 000.
	 */
	private HashMap<String, Integer> labelCallStack = new HashMap<>();
	
	/**
	 * Input handle
	 */
	private Scanner consoleScanner = new Scanner(System.in);
	
	/**
	 * starting keywords that signal that the line will be tokenized by spaces
	 * @see #tokenizeSimple(String)
	 */
	private String[] tokenizedBySpace = {"INT", "PRINTVAR", "CALL", "MUTABLE INT", "//", "GOTO", "LABEL"};
	
	/**
	 * Starting keywords that signal that the line will be tokenized by quotes
	 * @see #tokenizeQuotes(String)
	 */
	private String[] tokenizedByQuotes = {"STRING", "PRINTSTR"};
	
	/**
	 * Starting keywords that signal that the line will (ideally) be tokenized by braces
	 * @see #tokenizeBraces(String)
	 */
	private String[] tokenizedByBraces = {"IF", "FOR", "MACRO"};
	
	/**
	 * Starting keywords that signal to count the number of lines to execute (or not)
	 */
	private String[] countLinesBeforeExecuting = {"IF", "FOR", "MACRO"};
	
	/**
	 * Starting keywords that end IF and FOR.
	 */
	private String[] endingStrings = {"END IF", "END FOR", "END MACRO"};
	
	/**
	 * Memory to save the local int variables from macros.
	 * @implNote Interestingly, all the macros share one local memory.
	 * I can't imagine how that could be hacked...
	 * Yep its extremely scuffed and might stop working and kill every macro with local variables if it wants to.
	 */
	private HashMap<String, Integer> localIntVariables = new HashMap<>();
	
	/**
	 * Memory to save the local string variables from macros.
	 * @see #localIntVariables
	 */
	private HashMap<String, String> localStringVariables = new HashMap<>();
	
	/**
	 * Memory for the macro being executed at runtime
	 */
	private String[] macroCache;
	
	/**
	 * The type of cache. can be IF or FOR.
	 */
	private String cacheType = "";
	
	/**
	 * Cache for lines that need a condition to run.
	 */
	private String[] cache; //                  Nice
	
	/**
	 * The number of lines in the cache.
	 */
	private int cacheCount;
	
	/**
	 * If the cache exists this <b><i>should</i></b> be true
	 */
	private boolean hasCache = false;
	
	/**
	 * Whether or not the console indents your code.
	 */
	private boolean indented = false;
	
	/**
	 * When debug mode is on, it shows you which line the interpreter is on.
	 */
	private boolean debug = false;
	
	private int i = 0;
	
	/**
	 * Empty constructor
	 */
	public Interpreter() {}
	
	/**
	 * Unfinished code to hopefully be able to interpret lines of code read from a file.
	 * @param code the code, obviously
	 * @throws SimplerException Whenever a syntax error appears
	 * /@deprecated
	 */
	//@Deprecated
	public void interpretAndRun(String[] code) throws SimplerException {
		boolean isinmacro = false;
		hasCache = false;
		for (;i < code.length;) {
			if (debug) System.out.println("\nLine " + i + "\n");
			for (String token : countLinesBeforeExecuting) {
				if (code[i].indexOf("MACRO") == 0) {
					countMacroLines(code, i);
					i += macroMemory.get(code[i].split(" ")[1]).length;
					isinmacro = true;
					break;
				}
				if (code[i].indexOf(token) == 0) {
					countLines(code, i);
				}
			}
			if (!isinmacro) executeLine(tokenize(code[i]));
			isinmacro = false;
			if (hasCache) {
				i += cacheCount;
				++i;
			}
			i++;
		}
		clearMemory();
		clearCache();
	}
	
	/**
	 * Interprets code straight from the console input.
	 * @throws SimplerException Whenever a syntax error appears.
	 */
	public void interpretConsoleCode() throws SimplerException, StackOverflowError {
		boolean isinmacro = false;
		ArrayList<String> lines = new ArrayList<>();
		String[] currentCode;
		do {
			if (!indented) System.out.print(">>> ");
			else System.out.print("    >>> ");
			lines.add(consoleScanner.nextLine());
			if (lines.get(lines.size() - 1).indexOf("END") == 0) indented = false;
			for (String token : tokenizedByBraces) if (lines.get(lines.size() - 1).indexOf(token) == 0) indented = true;
		} while (lines.get(lines.size() - 1).length() > 1);
		String[] code = Strings.pop(Strings.toStringArray(lines.toArray()));
		hasCache = false;
		for (i = 0; i < code.length; i++) {
			checkStack();
			if (debug) System.out.println("\nLine " + i + "\n");
			for (String token : countLinesBeforeExecuting) {
				if (code[i].indexOf("MACRO") == 0) {
					countMacroLines(code, i);
					i += macroMemory.get(code[i].split(" ")[1]).length;
					isinmacro = true;
					break;
				}
				if (code[i].indexOf(token) == 0) {
					countLines(code, i);
				}
			}
			if (!isinmacro) executeLine(tokenize(code[i]));
			isinmacro = false;
			if (hasCache) {
				i += cacheCount;
				++i;
			}
		}
		clearMemory();
		clearCache();
	}
	
	/**
	 * Parent tokenizing method.
	 * @param line Line of code to tokenize
	 * @return String[] tokens
	 * @see #tokenizeSimple(String)
	 * @see #tokenizeQuotes(String)
	 * @see #tokenizeBraces(String)
	 */
	private String[] tokenize(String line) {
		String[] endingToken = {"end"};
		for (String startingToken : tokenizedBySpace) {
			if(line.indexOf(startingToken) == 0) return tokenizeSimple(line);
		} for (String startingToken : tokenizedByQuotes) {
			if(line.indexOf(startingToken) == 0) return tokenizeQuotes(line);
		} for (String startingToken : tokenizedByBraces) {
			if(line.indexOf(startingToken) == 0) return tokenizeBraces(line);
		} for (String startingToken : endingStrings) {
			if(line.indexOf(startingToken) == 0) return endingToken;
		} for (String startingToken : Strings.toStringArray(stringMemory.keySet().toArray())) {
			if(line.indexOf(startingToken) == 0) return tokenizeQuotes(line);
		} return tokenizeSimple(line);
	}
	
	/**
	 * Returns the line of code split by spaces
	 * @param line A line of code
	 * @return An array of strings representing the tokens
	 */
	private String[] tokenizeSimple(String line) {
		return line.split(" ");
	}
	
	private String[] tokenizeQuotes(String line) {
		ArrayList<String> tokens = new ArrayList<>();
		if(line.indexOf("PRINTSTR") == 0) {
			return new String[] {"PRINTSTR", line.split("\"")[1], line.split(" ")[line.split(" ").length - 1]};
		} else if(line.indexOf("STRING") == 0) {
			tokens.add("STRING");
			if(!line.split(" ")[2].equals("=")) return null;
			else tokens.add(line.split(" ")[1]);
			tokens.add("=");
			tokens.add(line.split("\"")[1]);
			return Strings.toStringArray(tokens.toArray());
		} for (String varname : Strings.toStringArray(stringMemory.keySet().toArray())) {
			if (line.indexOf(varname) == 0) return new String[] {line.split(" ")[0], line.split(" ")[1], line.split("\n")[1]};
		} return null;
	}
	
	/**
	 * Tokenize IF / FOR statements
	 * @param line the code line. format: [IF [variable name] [operator] [value] [regular statement]] OR [FOR [starting value] [end value] [regular statement]]
	 * @return tokens : String[]
	 */
	private String[] tokenizeBraces(String line) {
		ArrayList<String> tokens = new ArrayList<>();
		String tmp;
		int params = 0;
		if (line.indexOf("IF") == 0) {
			tokens.add("IF");
			tokens.add(line.split(" ")[1]);
			tokens.add(line.split(" ")[2]);
			tmp = line.split(" ")[3];
			try {
				int num = Integer.parseInt(tmp);
				tokens.add("int");
				tokens.add(num + "");
			} catch (NumberFormatException e) {
				tokens.add("string");
				tokens.add(line.split("\"")[1]);
			}
			return Strings.toStringArray(tokens.toArray());
		} else if (line.indexOf("FOR") == 0) {
			tokens.add("FOR");
			tokens.add(line.split(" ")[1]);
			if (line.split(" ")[2].equals("FROM")) {
				tokens.add("FROM");
				tokens.add(line.split(" ")[3]);
				if (line.split(" ")[4].equals("TO")) {
					tokens.add("TO");
					tokens.add(line.split(" ")[5]);
				} else {
					tokens.add("TO");
					tokens.add("NaN");
				}
				if (line.split(" ").length > 6) {
					tokens.add("INCREMENT");
					tokens.add(line.split(" ")[7]);
				}
			}
			return Strings.toStringArray(tokens.toArray());
		} else if (line.indexOf("START MACRO") == 0) {
			tokens.add("START MACRO");
			tokens.add(line.split(" ")[2]);
		}
		return Strings.toStringArray(tokens.toArray());
	}
	
	/**
	 * Executes the line of tokens given as an array.
	 * @param tokens A string array representing tokens.
	 * @throws SimplerException When a syntax error is raised.
	 */
	private void executeLine(String[] tokens) throws SimplerException {
		switch (tokens[0]) {
			case "INT": {
				addIntegerVariable(tokens);
				break;
			} case "STRING": {
				addStringVariable(tokens);
				break;
			} case "MUTABLE": {
				addMutableVariable(tokens);
			} case "PRINTVAR": {
				printVariable(tokens);
				break;
			} case "IF": {
				doChoice(tokens);
				break;
			} case "FOR": {
				loop(tokens);
				break;
			} case "//": {
				break;
			} case "PRINTSTR": {
				printString(tokens);
				break;
			} case "CALL": {
				callMacro(tokens);
				break;
			} case "end": {
				break;
			} case "LABEL": {
				addLabel(tokens);
				break;
			} case "GOTO": {
				jumptoLabel(tokens);
				break;
			} default: {
				if (tokens[0].indexOf("//") == 0) {
					break;
				} else if (integerMemory.containsKey(tokens[0])) {
					changeIntegerVariable(tokens);
					break;
				} else if (stringMemory.containsKey(tokens[0])) {
					changeStringVariable(tokens);
					break;
				} else if (mutableMemory.containsKey(tokens[0])) {
					changeMutableVariable(tokens);
					break;
				}
				throw new InterpreterError("Syntax Error: Unknown token: " + tokens[0]);
			}
		}
	}
	
	private void executeLine(String[] tokens, boolean inMacro) throws SimplerException {
		if (!inMacro) executeLine(tokens);
		switch (tokens[0]) {
			case "INT": {
				addIntegerVariable(tokens);
				break;
			} case "STRING": {
				addStringVariable(tokens);
				break;
			} case "MUTABLE": {
				addMutableVariable(tokens);
			} case "PRINTVAR": {
				printVariable(tokens);
				break;
			} case "IF": {
				doChoice(tokens);
				break;
			} case "FOR": {
				loop(tokens);
				break;
			} case "//": {
				break;
			} case "PRINTSTR": {
				printString(tokens);
				break;
			} case "CALL": {
				callMacro(tokens);
				break;
			} case "end": {
				break;
			} case "LABEL": {
				addLabel(tokens);
				break;
			} case "GOTO": {
				jumptoLabel(tokens);
				break;
			} default: {
				if (tokens[0].indexOf("//") == 0) {
					break;
				} else if (integerMemory.containsKey(tokens[0])) {
					changeIntegerVariable(tokens);
					break;
				} else if (stringMemory.containsKey(tokens[0])) {
					changeStringVariable(tokens);
					break;
				} else if (mutableMemory.containsKey(tokens[0])) {
					changeMutableVariable(tokens);
					break;
				}
				throw new InterpreterError("Syntax Error: Unknown token: " + tokens[0]);
			}
		}
	}
	
	/**
	 * Adds a variable to the integer memory
	 * @param tokens Tokens given by the tokenize() method
	 * @throws SimplerException when either an equals sign is not present or an existing variable name is used.
	 * @throws NumberFormatException when the string given cannot be parsed into an integer.
	 * @see #tokenize(String)
	 */
	private void addIntegerVariable(String[] tokens) throws SimplerException, NumberFormatException {
		if (!tokens[2].equals("=")) throw new SimplerException("Expected '=' sign!");
		else if (integerMemory.containsKey(tokens[1])) throw new SimplerException("Expected new variable name!");
		else {
			try {
				int num = Integer.parseInt(tokens[1]);
				throw new SimplerException("Expected valid variable name!");
			} catch (NumberFormatException e) {
				integerMemory.put(tokens[1], Integer.parseInt(tokens[3]));
			}
		}
	}
	
	private void addIntegerVariable(String[] tokens, boolean inMacro) throws NumberFormatException, SimplerException {
		if (!inMacro) addIntegerVariable(tokens);
		else if (localIntVariables.containsKey(tokens[1])) throw new SimplerException("Expected new variable name!");
		else {
			try {
				int num = Integer.parseInt(tokens[1]);
				num = Integer.parseInt(tokens[1].substring(0, 1));
				throw new SimplerException("Expected valid variable name!");
			} catch (NumberFormatException e) {
				localIntVariables.put(tokens[1], Integer.parseInt(tokens[3]));
			}
		}
	}
	
	private void addMutableVariable(String[] tokens) throws SimplerException {
		if (tokens.length > 3) {
			mutableMemory.put(tokens[2], Integer.parseInt(tokens[4]));
		} else {
			mutableMemory.put(tokens[2], null);
		}
	}
	
	private void changeMutableVariable(String[] tokens) throws SimplerException {
		if (!mutableMemory.containsKey(tokens[0])) throw new SimplerException("Expected existing mutable variable name!");
		mutableMemory.put(tokens[0], Integer.parseInt(tokens[2]));
	}
	
	/**
	 * Modifies an existing variable in the integer memory
	 * @param tokens Tokens given by the tokenize() method
	 * @throws SimplerException When a syntax error occurs
	 */
	private void changeIntegerVariable(String[] tokens) throws SimplerException {
		int val;
		if (!integerMemory.containsKey(tokens[0])) throw new InterpreterError("Sorry I messed up your code!");
		else {
			val = integerMemory.get(tokens[0]);
			switch (tokens[1]) {
				case "++": {
					integerMemory.put(tokens[0], val + 1);
					break;
				} case "--": {
					integerMemory.put(tokens[0], val - 1);
					break;
				} case "+=": {
					if (integerMemory.containsKey(tokens[2])) integerMemory.put(tokens[0], val + integerMemory.get(tokens[2]));
					else integerMemory.put(tokens[0], val + Integer.parseInt(tokens[2]));
					break;
				} case "-=": {
					if (integerMemory.containsKey(tokens[2])) integerMemory.put(tokens[0], val - integerMemory.get(tokens[2]));
					else integerMemory.put(tokens[0], val - Integer.parseInt(tokens[2]));
					break;
				} case "*=": {
					if (integerMemory.containsKey(tokens[2])) integerMemory.put(tokens[0], val * integerMemory.get(tokens[2]));
					else integerMemory.put(tokens[0], val * Integer.parseInt(tokens[2]));
					break;
				} case "/=": {
					if (integerMemory.containsKey(tokens[2])) integerMemory.put(tokens[0], val / integerMemory.get(tokens[2]));
					else integerMemory.put(tokens[0], val / Integer.parseInt(tokens[2]));
					break;
				} case "**=": {
					if (integerMemory.containsKey(tokens[2])) integerMemory.put(tokens[0], (int) Math.round(Math.pow(val, integerMemory.get(tokens[2]))));
					else integerMemory.put(tokens[0], (int) Math.round(Math.pow(val, Integer.parseInt(tokens[2]))));
					break;
				} default: {
					throw new SimplerException("Unknown operator!");
				}
			}
		}
	}
	
	/**
	 * Adds a variable to the string memory
	 * @param tokens Tokens given by the tokenize() method
	 * @throws SimplerException when either an equals sign is not present or an existing variable name is used.
	 * @see #tokenize(String)
	 */
	private void addStringVariable(String[] tokens) throws SimplerException {
		if (!tokens[2].equals("=")) throw new SimplerException("Expected '=' sign!");
		else if (stringMemory.containsKey(tokens[1])) throw new SimplerException("Expected new variable name!");
		else stringMemory.put(tokens[1], tokens[3]);
	}
	
	private void addStringVariable(String[] tokens, boolean inMacro) throws SimplerException {
		if (!inMacro) addStringVariable(tokens);
		if (!tokens[2].equals("=")) throw new SimplerException("Expected '=' sign!");
		else if (localStringVariables.containsKey(tokens[1])) throw new SimplerException("Expected new variable name!");
		else localStringVariables.put(tokens[1], tokens[3]);
	}
	
	private void changeStringVariable(String[] tokens) throws SimplerException {
		if (!tokens[1].equals("=")) throw new SimplerException("Expected '=' sign!");
		else if (!stringMemory.containsKey(tokens[0])) throw new SimplerException("Expected existing variable name!");
		stringMemory.put(tokens[0], tokens[2]);
	}
	
	private void printVariable(String[] tokens) throws SimplerException {
		Strings.printStringArray(Strings.toStringArray(mutableMemory.keySet().toArray()));
		String end = tokens[tokens.length - 1].equals("endl") ? "\n" : "";
		if (integerMemory.containsKey(tokens[1])) {
			System.out.print(integerMemory.get(tokens[1]) + end);
		} else if (stringMemory.containsKey(tokens[1])) {
			System.out.print(stringMemory.get(tokens[1]) + end);
		} else if (mutableMemory.containsKey(tokens[1])) {
			System.out.print(mutableMemory.get(tokens[1]) + end);
		} else {
			throw new SimplerException("Variable (Function) name not found!");
		}
	}
	
	private void printVariable(String[] tokens, boolean inMacro) throws SimplerException {
		String end = tokens[tokens.length - 1].equals("endl") ? "\n" : "";
		if (localIntVariables.containsKey(tokens[1])) {
			System.out.print(localIntVariables.get(tokens[1]) + end);
		} else if (localStringVariables.containsKey(tokens[1])) {
			System.out.print(localStringVariables.get(tokens[1]) + end);
		} else {
			throw new SimplerException("Variable (Function) name not found!");
		}
	}
	
	/**
	 * Function for evaluating IF statements in Simpler++.
	 */
	private void doChoice(String[] tokens) throws SimplerException {
		String[] blockTokens;
		boolean condition = false;
		//for(String token : tokens) System.out.printf("%s ", token);
		if (tokens[0].equals("IF")) {
			if (tokens[3].equals("int") && !integerMemory.containsKey(tokens[1])) throw new SimplerException("Expected existing variable name!");
			else if (tokens[3].equals("string") && !stringMemory.containsKey(tokens[1])) throw new SimplerException("Expected existing variable name!");
			condition = evaluate(tokens);
		} if (condition) {
			if (!cacheType.equals("IF")) throw new InterpreterError("Unknown interpreter error.");
			else for (String line : cache) {
				blockTokens = new String[cache.length];
				blockTokens = tokenize(line);
				if (blockTokens == null) break;
				executeLine(blockTokens);
			}
			return;
		} else {
			return;
		}
	}
	
	/**
	 * Puts a label into labelMemory.
	 * @param tokens Format: LABEL [label name]
	 */
	private void addLabel(String[] tokens) {
		labelMemory.put(tokens[1], i);
		labelCallStack.put(tokens[1], 0);
		return;
	}
	
	/**
	 * Jumps to the given label and increments the call stack accordingly.
	 * @param tokens Format: GOTO [label name]
	 */
	private void jumptoLabel(String[] tokens) {
		i = labelMemory.get(tokens[1]);
		labelCallStack.put(tokens[1], labelCallStack.get(tokens[1]) + 1);
	}
	
	private void checkStack() throws StackOverflowError {
		for (int i : labelCallStack.values()) {
			if (i > 10000) throw new StackOverflowError("Stack overflow");
		}
		return;
	}
	
	/**
	 * Evaluates a Simpler++ boolean expression based on String tokens.
	 * @param tokens The String array of tokens.
	 * @return a boolean value representing the evaluation of the tokens
	 * @throws InterpreterError If the lexer f*cks up
	 * @throws SimplerException If the lexer f*cks up
	 */
	private boolean evaluate(String[] tokens) throws InterpreterError, SimplerException {
		if (tokens[3].equals("int")) {
			if (tokens[2].equals("==")) return integerMemory.get(tokens[1]) == Integer.parseInt(tokens[4]);
			else if (tokens[2].equals("<")) return integerMemory.get(tokens[1]) < Integer.parseInt(tokens[4]);
			else if (tokens[2].equals(">")) return integerMemory.get(tokens[1]) > Integer.parseInt(tokens[4]);
			else if (tokens[2].equals(">=")) return integerMemory.get(tokens[1]) >= Integer.parseInt(tokens[4]);
			else if (tokens[2].equals("<=")) return integerMemory.get(tokens[1]) <= Integer.parseInt(tokens[4]);
			else if (tokens[2].equals("!=")) return integerMemory.get(tokens[1]) != Integer.parseInt(tokens[4]);
			else throw new SimplerException("Unknown operator!");
		} else if (tokens[3].equals("string")) {
			if (tokens[2].equals("==")) return stringMemory.get(tokens[1]).equals(tokens[4].substring(1, tokens[4].length() - 1));
			else if (tokens[2].equals("!=")) return !stringMemory.get(tokens[1]).equals(tokens[4].substring(1, tokens[4].length() - 1));
			else if (tokens[2].equals("<")) return stringMemory.get(tokens[1]).compareTo(tokens[4].substring(1, tokens[4].length() - 1)) < 0;
			else if (tokens[2].equals(">")) return stringMemory.get(tokens[1]).compareTo(tokens[4].substring(1, tokens[4].length() - 1)) > 0;
			else if (tokens[2].equals("<=")) return stringMemory.get(tokens[1]).compareTo(tokens[4].substring(1, tokens[4].length() - 1)) <= 0;
			else if (tokens[2].equals(">=")) return stringMemory.get(tokens[1]).compareTo(tokens[4].substring(1, tokens[4].length() - 1)) >= 0;
			else throw new SimplerException("Unknown operator!");
		} else {
			throw new InterpreterError("Unknown token error");
		}
	}
	
	private void loop(String[] tokens) throws SimplerException, NumberFormatException {
		int i = 0;
		int inc = 1;
		if (tokens.length > 6) inc = Integer.parseInt(tokens[7]);
		if (tokens[5].equals("NaN")) {
			i = Integer.parseInt(tokens[3]);
			while (true) {
				integerMemory.put(tokens[1], i);
				i += inc;
				for (String line : cache) {
					executeLine(tokenize(line));
				}
			}
		}
		for(i = Integer.parseInt(tokens[3]); i <= Integer.parseInt(tokens[5]); i += inc) {
			integerMemory.put(tokens[1], i);
			for (String line : cache) {
				executeLine(tokenize(line));
			}
		}
 	}
	
	private void callMacro(String[] tokens) throws SimplerException {
		if (!macroMemory.containsKey(tokens[1])) throw new SimplerException("Macro " + tokens[1] + " not found");
		String[] code = macroMemory.get(tokens[1]);
		for (int i = 0; i < macroMemory.get(tokens[1]).length; i++) {
			if (debug) System.out.println("Line " + i);
			for (String token : countLinesBeforeExecuting) {
				if (code[i].indexOf(token) == 0) {
					countLines(code, i);
				}
			}
			executeLine(tokenize(code[i]), true);
			if (hasCache) {
				i += cacheCount;
				++i;
			}
		}
	}
	
	private void countLines(String[] lines, int startingLine) {
		String keyword = lines[startingLine].split(" ")[0];
		cacheType = keyword;
		cacheCount = 0;
		for(int i = ++startingLine; i < lines.length;) {
			if (lines[i].indexOf("END " + keyword) == 0) break;
			cacheCount++;
			++i;
		}
		cache = new String[cacheCount];
		for(int i = 0; i < cacheCount; i++) {
			cache[i] = lines[i + startingLine];
		}
		hasCache = true;
	}
	
	private void countMacroLines(String[] lines, int startingLine) {
		int i = startingLine + 1;
		ArrayList<String> macro = new ArrayList<>();
		do {
			macro.add(lines[i]);
		} while(lines[i++].indexOf("END MACRO") != 0);
		macro.removeLast();
		macroMemory.put(lines[startingLine].split(" ")[1], Strings.toStringArray(macro.toArray()));
	}
	
	private void printString(String[] tokens) {
		if (tokens.length > 1) {
			if (tokens[2].equals("endl")) {
				System.out.println(tokens[1]);
				return;
			} else {
				System.out.print(tokens[1]);
			}
		} else {
			System.out.print(tokens[1]);
		}
	}
	
	public void clearCache() {
		if (!hasCache) return;
		cacheType = "";
		cacheCount = 0;
		cache = new String[0];
		macroCache = new String[0];
		hasCache = false;
	}
	
	public void clearMemory() {
		integerMemory.clear();
		stringMemory.clear();
		macroMemory.clear();
		labelMemory.clear();
		mutableMemory.clear();
		labelCallStack.clear();
		localIntVariables.clear();
		localStringVariables.clear();
		i = 0;
	}
	
	public void toggleDebug() {
		debug = !debug;
	}
}

