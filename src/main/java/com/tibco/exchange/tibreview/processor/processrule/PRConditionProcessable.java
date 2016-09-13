package com.tibco.exchange.tibreview.processor.processrule;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.rules.Rule;

public interface PRConditionProcessable {
	public boolean processCondition(Context context, TIBProcess process, Rule rule, Object impl) throws ProcessorException;
}
