package com.tibco.exchange.tibreview.common;

import com.tibco.exchange.tibreview.engine.Context;

public abstract class ContextReplaceable {
	public String replace(String s, Context context) {
		String replacement = replaceAction(s, context);
		if(replacement == null) {
			throw new IllegalArgumentException(errorMessage(s));
		}
		return replacement;
	}
	
	public abstract String replaceAction(String s, Context context);
	
	public abstract String errorMessage(String s);
}
