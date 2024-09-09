package com.turnipcorp.Simpler.console;

import java.io.FileNotFoundException;
import java.io.LineNumberReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;

import com.turnipcorp.Simpler.interpreter.Strings;

public class SimplerFileHandler {
	
	private LineNumberReader reader;
	
	private FileWriter writer;
	
	/**
	 * Empty constructor
	 */
	public SimplerFileHandler() {}
	
	/**
	 * Reads a file and returns a array of lines.
	 * @param filename the path to the file
	 * @return An array of strings that represents all the lines in the file
	 * @throws FileNotFoundException if the file does not exist
	 * @throws IOException If some sort of error occured while reading the file
	 */
	public String[] read(String filename) throws FileNotFoundException, IOException {
		reader = new LineNumberReader(new FileReader(filename));
		String buffer;
		ArrayList<String> result = new ArrayList<>();;
		do {
			buffer = reader.readLine();
			result.add(buffer);
		} while (buffer != null);
		reader.close();
		return Strings.pop(Strings.toStringArray(result.toArray()));
	}
	
	/**
	 * Writes an array of lines to a file.
	 * @param filename the path to the file
	 * @param toWrite An array of strings that represents all the lines to write to the file
	 * @throws IOException If some sort of error occurs while trying to write to the file
	 */
	public void write(String filename, String[] toWrite) throws IOException {
		writer = new FileWriter(filename);
		for(String line : toWrite) {
			writer.write(line + "\n");
		}
		writer.close();
	}
}
