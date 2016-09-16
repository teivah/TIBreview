package com.tibco.exchange.tibreview.processor.resourcerule;

import java.util.List;

import com.tibco.exchange.tibreview.common.TIBResource;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Configuration;
import com.tibco.exchange.tibreview.model.rules.Resourcerule;

public interface RRProcessable {
	public List<Violation> process(Context context, TIBResource resource, Resourcerule rule, Configuration configuration) throws ProcessorException;
}
