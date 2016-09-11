package com.tibco.exchange.tibreview.exception;

public class ParsingException extends Exception {

	private static final long serialVersionUID = 89526587L;

	public ParsingException() {
		super();
	}
	
	public ParsingException(String message) {
		super(message);
	}
	
	public ParsingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ParsingException(Throwable cause) {
		super(cause);
	}
}
