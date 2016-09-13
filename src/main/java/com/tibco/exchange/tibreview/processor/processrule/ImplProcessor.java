package com.tibco.exchange.tibreview.processor.processrule;

import java.util.List;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Impl;
import com.tibco.exchange.tibreview.model.rules.Rule;

public class ImplProcessor implements PRProcessable {
	@Override
	public List<Violation> process(Context context, TIBProcess process, Rule rule, Object impl) throws ProcessorException {
		Impl el = (Impl) impl;
		if(el.getCond() != null) {
			CondProcessor processor = new CondProcessor();
			return processor.process(context, process, rule, el.getCond());
		} else if(el.getNot() != null) {
			NotProcessor processor = new NotProcessor();
			return processor.process(context, process, rule, el.getNot());
		} else { //Xpath
			XPathProcessor processor = XPathProcessor.getInstance();
			return processor.process(context, process, rule, el.getXpath()); 
		}
	}
}
