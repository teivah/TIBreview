package com.tibco.exchange.tibreview.javarule;

import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.view.TIBProcess;

public interface Executable {
	public boolean execute(Context context, TIBProcess process, Object impl);
}
