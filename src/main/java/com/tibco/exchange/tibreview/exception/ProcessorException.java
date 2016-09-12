package com.tibco.exchange.tibreview.exception;

public class ProcessorException extends Exception {

	private static final long serialVersionUID = 89526587L;

	public ProcessorException() {
		super();
	}
	
	public ProcessorException(String message) {
		super(message);
	}
	
	public ProcessorException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ProcessorException(Throwable cause) {
		super(cause);
	}
}
