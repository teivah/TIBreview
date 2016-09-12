package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.model.Elseif;
import com.tibco.exchange.tibreview.view.TIBProcess;

public final class ElseifProcessor implements Processable, ConditionProcessable {
	@Override
	public boolean process(TIBProcess process, Object impl) {
		Elseif el = (Elseif) impl;
		ImplProcessor processor = new ImplProcessor();
		return processor.process(process, el.getThen());
	}

	@Override
	public boolean processCondition(TIBProcess process, Object impl) {
		Elseif el = (Elseif) impl;
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
