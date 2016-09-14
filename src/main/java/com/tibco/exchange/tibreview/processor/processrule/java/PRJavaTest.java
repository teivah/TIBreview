package com.tibco.exchange.tibreview.processor.processrule.java;

import java.util.List;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Rule;

public class PRJavaTest extends PRJava {

	@Override
	public List<Violation> process(Context context, TIBProcess process, Rule rule, Object impl)
			throws ProcessorException {
		System.out.println("Java rule: context=" + context + ", process=" + process + ", rule=" + rule + ", impl=" + impl);
		return null;
	}
	
	@Override
	public boolean processCondition(Context context, TIBProcess process, Rule rule, Object impl)
			throws ProcessorException {
		return true;
	}
}
