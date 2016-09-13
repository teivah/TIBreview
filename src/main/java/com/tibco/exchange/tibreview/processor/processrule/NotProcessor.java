package com.tibco.exchange.tibreview.processor.processrule;

import java.util.ArrayList;
import java.util.List;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Not;
import com.tibco.exchange.tibreview.model.rules.Rule;

public final class NotProcessor implements PRProcessable {
	@Override
	public List<Violation> process(Context context, TIBProcess process, Rule rule, Object impl) throws ProcessorException {
		Not el = (Not) impl;
		
		List<Violation> violations = null;
		if(el.getCond() != null) {
			CondProcessor processor = new CondProcessor();
			violations = processor.process(context, process, rule, el.getCond());
		} else if(el.getNot() != null) {
			NotProcessor processor = new NotProcessor();
			violations = processor.process(context, process, rule, el.getNot());
		} else { //Xpath
			XPathProcessor processor = XPathProcessor.getInstance();
			violations = processor.process(context, process, rule, el.getXpath()); 
		}
		
		if(violations == null || violations.size() == 0) {
			//Format a violation
			violations = new ArrayList<>();
			violations.add(Util.formatViolation(rule));
			return violations;
		} else {
			return null;
		}
	}
}
