package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.Impl;
import com.tibco.exchange.tibreview.view.TIBProcess;

public class ImplProcessor implements Processable {
	@Override
	public boolean process(Context context, TIBProcess process, Object impl) throws ProcessorException {
		Impl el = (Impl) impl;
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
