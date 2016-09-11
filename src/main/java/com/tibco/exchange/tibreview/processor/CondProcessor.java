package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.Cond;

public final class CondProcessor implements Processable {
	@Override
	public boolean process(String file, Object impl) {
		Cond el = (Cond) impl;
		return false;
	}
}
