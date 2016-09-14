package com.tibco.exchange.tibreview.engine;

import com.tibco.exchange.tibreview.common.PMDManager;
import com.tibco.exchange.tibreview.exception.EngineException;
import com.tibco.exchange.tibreview.model.rules.Tibrules;

public interface AssetProcessable {
	public void process(Context context, Tibrules tibrules, PMDManager manager) throws EngineException;
}
