package com.tibco.exchange.tibreview.javarule;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;

public interface Executable {
	public boolean execute(Context context, TIBProcess process, Object impl);
}
