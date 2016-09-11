package com.tibco.exchange.tibreview.processor;

public interface ConditionProcessable {
	public boolean processCondition(String file, Object impl);
}
