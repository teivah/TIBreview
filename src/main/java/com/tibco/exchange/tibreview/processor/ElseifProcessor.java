package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.Elseif;

public final class ElseifProcessor implements Processable {
	@Override
	public boolean process(String file, Object impl) {
		Elseif el = (Elseif) impl;
		return false;
	}
}
