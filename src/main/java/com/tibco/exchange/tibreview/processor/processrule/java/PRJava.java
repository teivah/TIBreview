package com.tibco.exchange.tibreview.processor.processrule.java;

import java.util.List;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Rule;
import com.tibco.exchange.tibreview.processor.processrule.PRConditionProcessable;
import com.tibco.exchange.tibreview.processor.processrule.PRGloballyProcessable;
import com.tibco.exchange.tibreview.processor.processrule.PRProcessable;

public abstract class PRJava implements PRProcessable, PRConditionProcessable, PRGloballyProcessable {
	@Override
	public List<Violation> process(Context context, TIBProcess process, Rule rule, Object impl)
			throws ProcessorException {
		//Default implementation
		return null;
	}
	
	@Override
	public boolean processCondition(Context context, TIBProcess process, Rule rule, Object impl)
			throws ProcessorException {
		//Default implementation
		List<Violation> violations = process(context, process, rule, impl);
		return violations == null || violations.size() == 0;
	}
	
	@Override
	public List<Violation> process(Context context, List<TIBProcess> processes, Rule rule, Object impl)
			throws ProcessorException {
		//Default implementation
		return null;
	}
}
