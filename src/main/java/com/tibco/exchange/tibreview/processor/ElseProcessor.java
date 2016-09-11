package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.Else;

public final class ElseProcessor implements Processable {
	@Override
	public boolean process(String file, Object impl) {
		Else el = (Else) impl;
		return false;
	}
}
