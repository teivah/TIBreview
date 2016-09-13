package com.tibco.exchange.tibreview.processor.processrule;

import java.util.List;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.If;
import com.tibco.exchange.tibreview.model.rules.Rule;

public final class IfProcessor implements PRProcessable, PRConditionProcessable {
	@Override
	public List<Violation> process(Context context, TIBProcess process, Rule rule, Object impl) throws ProcessorException {
		If el = (If) impl;
		ImplProcessor processor = new ImplProcessor();
		return processor.process(context, process, rule, el.getThen());
	}

	@Override
	public boolean processCondition(Context context, TIBProcess process, Rule rule, Object impl) throws ProcessorException {
		If el = (If) impl;
		if(el.getCond() != null) {
			CondProcessor processor = new CondProcessor();
			List<Violation> violations = processor.process(context, process, rule, el.getCond());
			return violations == null || violations.size() == 0;
		} else if(el.getNot() != null) {
			NotProcessor processor = new NotProcessor();
			List<Violation> violations = processor.process(context, process, rule, el.getNot());
			return violations == null || violations.size() == 0;
		} else if(el.getXpath() != null) {
			XPathProcessor processor = XPathProcessor.getInstance();
			List<Violation> violations = processor.process(context, process, rule, el.getXpath());
			return violations == null || violations.size() == 0;
		} else if(el.getJava() != null) {
			JavaProcessor processor = new JavaProcessor();
			List<Violation> violations = processor.process(context, process, rule, el.getJava());
			return violations == null || violations.size() == 0;
		} else {
			return false;
		}
	}
}
