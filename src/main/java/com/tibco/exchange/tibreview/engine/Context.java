package com.tibco.exchange.tibreview.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import com.tibco.exchange.tibreview.common.Util;

public class Context {
	private Map<String, String> xpathFunctions;
	private Map<String, String> disabledRules;
	private Map<String, String> properties;
	private String source;
	private String inputType;
	private LinkedList<String> filenames;

	public Context() {
		this.xpathFunctions = new HashMap<>();
		this.disabledRules = new HashMap<>();
		this.properties = new HashMap<>();
		this.filenames = new LinkedList<>();
	}

	public Map<String, String> getXpathFunctions() {
		return xpathFunctions;
	}

	public void setXpathFunctions(Map<String, String> xpathFunctions) {
		this.xpathFunctions = xpathFunctions;
	}

	public Map<String, String> getDisabledRules() {
		return disabledRules;
	}

	public void setDisabledRules(Map<String, String> disabledRules) {
		this.disabledRules = disabledRules;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public LinkedList<String> getFilenames() {
		return filenames;
	}
	
	@Override
	public String toString() {
		return "Context [xpathFunctions=" + xpathFunctions + ", disabledRules=" + disabledRules + ", properties="
				+ properties + ", source=" + source + ", inputType=" + inputType + "]";
	}
}
