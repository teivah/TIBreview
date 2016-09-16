package com.tibco.exchange.tibreview.exception;

public class EngineException extends Exception {

	private static final long serialVersionUID = 89526587L;

	public EngineException() {
		super();
	}
	
	public EngineException(String message) {
		super(message);
	}
	
	public EngineException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EngineException(Throwable cause) {
		super(cause);
	}
}
