package com.tibco.exchange.tibreview.processor;

import com.tibco.exchange.tibreview.view.TIBProcess;

public interface Processable {
	public boolean process(TIBProcess process, Object impl);
}
