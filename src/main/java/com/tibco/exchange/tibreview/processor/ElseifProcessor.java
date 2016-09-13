package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.rules.Elseif;
import com.tibco.exchange.tibreview.view.TIBProcess;

public final class ElseifProcessor implements Processable, ConditionProcessable {
	@Override
	public boolean process(Context context, TIBProcess process, Object impl) throws ProcessorException {
		Elseif el = (Elseif) impl;
		ImplProcessor processor = new ImplProcessor();
		return processor.process(context, process, el.getThen());
	}

	@Override
	public boolean processCondition(Context context, TIBProcess process, Object impl) throws ProcessorException {
		Elseif el = (Elseif) impl;
		if(el.getCond() != null) {
			CondProcessor processor = new CondProcessor();
			return processor.process(context, process, el.getCond());
		} else if(el.getNot() != null) {
			NotProcessor processor = new NotProcessor();
			return processor.process(context, process, el.getNot());
		} else { //Xpath
			XPathProcessor processor = XPathProcessor.getInstance();
			return processor.process(context, process, el.getXpath()); 
		}
	}
}
