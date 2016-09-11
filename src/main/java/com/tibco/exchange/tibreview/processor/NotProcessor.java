package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.Impl;
import com.tibco.exchange.tibreview.model.Not;

public final class NotProcessor implements Processable {
	@Override
	public boolean process(String file, Object impl) {
		Not el = (Not) impl;
		
		if(el.getCond() != null) {
			CondProcessor processor = new CondProcessor();
			return !processor.process(file, el.getCond());
		} else if(el.getNot() != null) {
			NotProcessor processor = new NotProcessor();
			return !processor.process(file, el.getNot());
		} else { //Xpath
			XPathProcessor processor = XPathProcessor.getInstance();
			return !processor.process(file, el.getXpath()); 
		}
	}
}
