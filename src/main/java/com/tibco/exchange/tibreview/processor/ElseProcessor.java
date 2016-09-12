package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.model.Else;
import com.tibco.exchange.tibreview.view.TIBProcess;

public final class ElseProcessor implements Processable {
	@Override
	public boolean process(Context context, TIBProcess process, Object impl) {
		Else el = (Else) impl;
		ImplProcessor processor = new ImplProcessor();
		return processor.process(context, process, el.getThen());
	}
}
