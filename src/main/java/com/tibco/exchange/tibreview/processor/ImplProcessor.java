package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.Impl;
import com.tibco.exchange.tibreview.view.TIBProcess;

public class ImplProcessor implements Processable {
	@Override
	public boolean process(TIBProcess process, Object impl) {
		Impl el = (Impl) impl;
		if(el.getCond() != null) {
			CondProcessor processor = new CondProcessor();
			return processor.process(process, el.getCond());
		} else if(el.getNot() != null) {
			NotProcessor processor = new NotProcessor();
			return processor.process(process, el.getNot());
		} else { //Xpath
			XPathProcessor processor = XPathProcessor.getInstance();
			return processor.process(process, el.getXpath()); 
		}
	}
}
