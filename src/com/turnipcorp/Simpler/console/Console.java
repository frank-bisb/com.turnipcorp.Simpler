package com.turnipcorp.Simpler.console;

import java.util.Scanner;

import com.turnipcorp.Simpler.interpreter.Interpreter;
import com.turnipcorp.Simpler.lang.Logger;
import com.turnipcorp.Simpler.lang.TerminatedError;
import com.turnipcorp.Simpler.main.Main;

public class Console {
	
	private int cmdNumber = 0;
	
	private String versionInfo = "Prerelease Version 0.5.4";
	
	/**
	 * Interpreter object
	 */
	public Interpreter interpreter = new Interpreter();
	
	/**
	 * File handler object
	 */
	public SimplerFileHandler fileHandler = new SimplerFileHandler();
	
	/**
	 * Scanner for System.in
	 */
	private Scanner consoleScanner = new Scanner(System.in);
	
	/**
	 * Echo, like cmd::echo.
	 */
	private boolean echo = true;
	
	/**
	 * Empty constructor
	 */
	public Console() {}
	
	public void getCommand() throws Exception, TerminatedError {
		if (cmdNumber++ != 0) System.out.println();
		if (echo) System.out.print("Simpler++Console> ");
		exec(consoleScanner.nextLine());
	}
	
	/**
	 * Executes a console command.
	 * @param cmd the command : String
	 * @throws Exception when an exception is thrown by the interpreter.
	 */
	public void exec(String cmd) throws Exception, TerminatedError {
		Logger.info("Executing command: \"" + cmd + "\"");
		String[] params = cmd.split(" ");
		switch (params[0]) {
			/*case "-run": {
				interpreter.interpretAndRun(this.fileHandler.read(params[1]));
				break;
			}*/ case "interpret": {
				interpreter.interpretConsoleCode();
				break;
			} case "exit": {
				Logger.info("Exited");
				Logger.Destruct();
				System.exit(0);
			} case "echo": {
				if (params[1].equals("off")) echo = false;
				else echo = true;
			} case "debug": {
				interpreter.toggleDebug();
				break;
			} case "help": {
				printHelp();
				break;
			} case "version": {
				printVersionInfo();
				break;
			} case "dev::crash": {
				Main.crash();
				throw new TerminatedError("Hmm...");
			} case "dev::testfile": {
				interpreter.interpretAndRun(fileHandler.read(params[1]));
			} default: {
				Logger.error("Command \"" + cmd + "\" " + "not found.");
			}
		}
	}
	
	private void printHelp() {
		System.out.println("> interpret: Interprets code entered from the console");
		System.out.println("> echo [on/off]: Works like echo on/off in windows cmd.");
		System.out.println("> debug: Notifies you which line it is executing.");
		System.out.println("> help: Shows this menu.");
		System.out.println("> exit: Exits this program and destructs the logger.");
		System.out.println("> version: Shows version information.");
	}
	
	private void printVersionInfo() {
		System.out.println(versionInfo);
	}
}
