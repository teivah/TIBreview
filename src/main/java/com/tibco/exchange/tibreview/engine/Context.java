package com.tibco.exchange.tibreview.engine;

import java.util.HashMap;
import java.util.Map;

import com.tibco.exchange.tibreview.common.Util;

public class Context {
	private Map<String, String> xpathFunctions;
	private Map<String, String> disabledRules;
	private Map<String, String> properties;

	public Context() {
		this.xpathFunctions = new HashMap<>();
		this.disabledRules = new HashMap<>();
		this.properties = new HashMap<>();
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

	@Override
	public String toString() {
		return "Context [xpathFunctions=" + Util.mapToString(xpathFunctions) + ", disabledRules="
				+ Util.mapToString(disabledRules) + ", properties=" + Util.mapToString(properties) + "]";
	}

}
