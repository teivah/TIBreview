package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.Not;
import com.tibco.exchange.tibreview.view.TIBProcess;

public final class NotProcessor implements Processable {
	@Override
	public boolean process(TIBProcess process, Object impl) {
		Not el = (Not) impl;
		
		if(el.getCond() != null) {
			CondProcessor processor = new CondProcessor();
			return !processor.process(process, el.getCond());
		} else if(el.getNot() != null) {
			NotProcessor processor = new NotProcessor();
			return !processor.process(process, el.getNot());
		} else { //Xpath
			XPathProcessor processor = XPathProcessor.getInstance();
			return !processor.process(process, el.getXpath()); 
		}
	}
}
