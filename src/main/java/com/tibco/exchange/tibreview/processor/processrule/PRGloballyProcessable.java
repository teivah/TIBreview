package com.tibco.exchange.tibreview.processor.processrule;

import java.util.List;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Rule;

public interface PRGloballyProcessable {
	public List<Violation> process(Context context, List<TIBProcess> processes, Rule rule, Object impl) throws ProcessorException;
}
