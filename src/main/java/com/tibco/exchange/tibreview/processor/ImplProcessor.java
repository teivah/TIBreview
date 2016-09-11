package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.Impl;

public class ImplProcessor implements Processable {
	@Override
	public boolean process(String file, Object impl) {
		Impl el = (Impl) impl;
		if(el.getCond() != null) {
			
		} else if(el.getNot() != null) {
			
		} else { //Xpath
			XPathProcessor processor = XPathProcessor.getInstance();
			return processor.process(file, el.getXpath()); 
		}
		return false;
	}
}
