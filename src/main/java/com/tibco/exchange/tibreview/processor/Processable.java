package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.view.TIBProcess;

public interface Processable {
	public boolean process(Context context, TIBProcess process, Object impl) throws ProcessorException;
}
