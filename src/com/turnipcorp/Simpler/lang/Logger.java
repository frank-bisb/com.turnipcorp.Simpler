package com.turnipcorp.Simpler.lang;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
	
	/**
	 * File path to the log file
	 */
	private static String logFile = null;
	
	/**
	 * FileWriter that writes to the log file
	 */
	private static OutputStream logWriter;
	
	private static String getTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss:SSSS"));
	}
	
	/**
	 * Static pseudo-constructor for the class.
	 * @param filename The log file to write to
	 */
	public static void Construct(String filename) {
		logFile = filename;
		try {
			logWriter = Files.newOutputStream(Paths.get(logFile), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			logWriter.write("\n\n".getBytes());
		} catch (IOException e) {
			System.out.println("Could not open log file successfully. Please check if the file is already opened, and restart the program.");
			System.exit(1);
		}
	}
	
	public static void info(String msg) {
		try {
			logWriter.write((getTime() + " [INFO] " + msg + "\n").getBytes());
		} catch (IOException e) {
			System.out.println("Could not log to file successfully.");
		}
	}
	
	public static void error(String msg) {
		try {
			logWriter.write((getTime() + " [ERROR] " + msg + "\n").getBytes());
		} catch (IOException e) {
			System.out.println("Could not log to file successfully.");
		}
	}
	
	public static void fatal(String msg) {
		try {
			logWriter.write((getTime() + " [FATAL] " + msg + "\n").getBytes());
		} catch (IOException e) {
			System.out.println("Could not log to file successfully.");
		}
	}
	
	/**
	 * Static pseudo-destructor of the logger class.
	 */
	public static void Destruct() {
		try {
			logWriter.close();
		} catch (IOException e) {
			System.out.println("Could not close logger.");
		}
	}
}
