package com.turnipcorp.Simpler.lang;

public class TerminatedError extends java.lang.Error {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 0x7FFFFFL;
	
	/**
	 * Error message
	 * @see #getMessage()
	 */
	private String msg;
	
	/**
	 * Constructor for TerminatedError class
	 * @param arg message: String
	 */
	public TerminatedError(String arg) {super(arg); msg = arg;}
	
	/**
	 * Returns the message given in the constructor
	 */
	@Override
	public String getMessage() {
		return msg;
	}
	
	/**
	 * calls Throwable.printStackTrace on System.out
	 * @see #Throwable.printStackTrace(PrintStream)
	 */
	@Override
	public void printStackTrace() {
		super.printStackTrace(System.out);
	}
}
