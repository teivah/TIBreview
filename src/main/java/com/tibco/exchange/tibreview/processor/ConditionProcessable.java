package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.view.TIBProcess;

public interface ConditionProcessable {
	public boolean processCondition(Context context, TIBProcess process, Object impl);
}
