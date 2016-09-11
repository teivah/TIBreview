package com.tibco.exchange.tibreview.exception;

public class XPathProcessorException extends Exception {

	private static final long serialVersionUID = 89526587L;

	public XPathProcessorException() {
		super();
	}
	
	public XPathProcessorException(String message) {
		super(message);
	}
	
	public XPathProcessorException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public XPathProcessorException(Throwable cause) {
		super(cause);
	}
}
