package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.view.TIBProcess;

public interface ConditionProcessable {
	public boolean processCondition(TIBProcess process, Object impl);
}
