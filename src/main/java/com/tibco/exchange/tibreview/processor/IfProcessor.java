package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.If;

public final class IfProcessor implements Processable, ConditionProcessable {
	@Override
	public boolean process(String file, Object impl) {
		If el = (If) impl;
		return false;
	}

	@Override
	public boolean processCondition(String file, Object impl) {
		// TODO Auto-generated method stub
		return false;
	}
}
