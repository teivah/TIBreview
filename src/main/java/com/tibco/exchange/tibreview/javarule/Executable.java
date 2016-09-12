package com.tibco.exchange.tibreview.javarule;

import com.tibco.exchange.tibreview.view.TIBProcess;

public interface Executable {
	public boolean execute(TIBProcess process, Object impl);
}
