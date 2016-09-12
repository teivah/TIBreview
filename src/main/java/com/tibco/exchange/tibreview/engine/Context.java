package com.tibco.exchange.tibreview.engine;

import java.util.Map;

public class Context {
	private Map<String, String> xpathFunctions;
	
	public Context(Map<String, String> xpathFunctions) {
		super();
		this.xpathFunctions = xpathFunctions;
	}

	public Map<String, String> getXpathFunctions() {
		return xpathFunctions;
	}
	
	public void setXpathFunctions(Map<String, String> xpathFunctions) {
		this.xpathFunctions = xpathFunctions;
	}
}
