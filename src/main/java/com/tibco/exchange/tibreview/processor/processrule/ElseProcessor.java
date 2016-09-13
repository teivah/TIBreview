package com.tibco.exchange.tibreview.processor.processrule;

import java.util.List;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Else;
import com.tibco.exchange.tibreview.model.rules.Rule;

public final class ElseProcessor implements PRProcessable {
	@Override
	public List<Violation> process(Context context, TIBProcess process, Rule rule, Object impl) throws ProcessorException {
		Else el = (Else) impl;
		ImplProcessor processor = new ImplProcessor();
		return processor.process(context, process, rule, el.getThen());
	}
}
