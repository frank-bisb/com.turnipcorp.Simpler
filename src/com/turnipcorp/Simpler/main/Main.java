package com.turnipcorp.Simpler.main;

import java.util.Random;

import com.turnipcorp.Simpler.console.Console;
import com.turnipcorp.Simpler.lang.Logger;
import com.turnipcorp.Simpler.lang.TerminatedError;

/**
 * The main class for Dai's Simpler++.
 * <br /> <br />
 * <b><i>&emsp;&emsp;No more parentheses, square brackets, or curly braces anymore!<i></b>
 * <br />&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
 * &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
 * &nbsp;&nbsp;&nbsp;-Frank, 2024
 * @implNote com.turnipcorp.SimplerReloaded is on hold right now due to lexer issues.
 * @implNote The fact that there are no bracees is definetly a feature and not a fatal design flaw.
 * @see com.turnipcorp.Simpler.interpreterInterpreter
 */
public class Main {
	
	/**
	 * Console object
	 */
	private static Console console = new Console();
	
	/**
	 * Main driver code
	 * Constructs the logger, then runs run() while true.
	 * @param args Console arguments
	 * @see #run()
	 */
	public static void main(String[] args) {
		Logger.Construct("E:/Summer2024/code/cty/java/com.turnipcorp.Simpler/SimplerMostRecent.log");
		Logger.info("Logger created");
		while (true) {
			run();
		}
	}
	
	/**
	 * Exactly what the name tells you.
	 * :D
	 * Repeatedly calls stackOverflow in a try-catch block for a random number of times between 2500 and 5000.
	 * Logs to the log with FATAL priority: Forcing crash... procedure _[number]_
	 */
	public static void crash() {
		int c = 0;
		Random r = new Random();
		int d = r.nextInt(2500, 5000);
		try {
			while (true) {
				if (c >= d) {
					Logger.fatal("Crashed :D");
					throw new TerminatedError("Crashed :D");
				}
				try {
					stackOverflow();
				} catch (Throwable e) {
					Logger.fatal("Forcing crash... procedure " + ++c);
					continue;
				}
			}
		} catch (TerminatedError e) {
			System.err.println("Crashed :D");
			System.exit(1);
		}
	}
	
	/**
	 * Causes a stack overflow.
	 * @see #crash()
	 */
	public static void stackOverflow() {
		stackOverflow();
	}
	
	/**
	 * Driver code that handles errors;
	 * catches:
	 * <ul>
	 * <li> Exception [Mostly Simpler]: Prints the stack trace and logs the message.
	 * <li> StackoverflowError: Responds by printing the message to System.out and the log
	 * <li> TerminatedError [Simpler]: Responds by System.exit(1) :P
	 * <li> Error [Mostly Simpler]: Prints the message and logs it.
	 * <li> Any other throwable: Prints the message, logs it with FATAL priority, and exits
	 * </ul>
	 */
	private static void run() {
		try {
			console();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception occured, stack trace above.");
			System.err.println("Show the stack trace to the developer, and he might fix it, or not.");
			Logger.error(e.getMessage());
			console.interpreter.clearMemory();
			console.interpreter.clearCache();
		} catch (StackOverflowError e) {
			System.err.println("\n" + e.getMessage());
			Logger.error(e.getMessage());
		} catch (TerminatedError e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Error e) {
			System.err.println(e.getMessage());
			Logger.error(e.getMessage());
		} catch (Throwable e) {
			System.err.println(e.getMessage());
			Logger.fatal(e.getMessage());
			System.exit(0);
		}
	}
	
	/**
	 * Driver code that contains the main loop
	 * @throws Exception When an exception is thrown when executing the code.
	 */
	private static void console() throws Exception {
		while (true) {
			console.interpreter.clearMemory();
			console.interpreter.clearCache();
			console.getCommand();
			console.interpreter.clearMemory();
			console.interpreter.clearCache();
		}
	}
}
