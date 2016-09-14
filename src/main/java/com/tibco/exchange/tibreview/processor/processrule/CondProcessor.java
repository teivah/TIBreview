package com.tibco.exchange.tibreview.processor.processrule;

import java.util.List;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.engine.Engine;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Cond;
import com.tibco.exchange.tibreview.model.rules.Else;
import com.tibco.exchange.tibreview.model.rules.Elseif;
import com.tibco.exchange.tibreview.model.rules.If;
import com.tibco.exchange.tibreview.model.rules.Rule;

public final class CondProcessor implements PRProcessable {
	private static final Logger LOGGER = Logger.getLogger(Engine.class);
	
	@Override
	public List<Violation> process(Context context, TIBProcess process, Rule rule, Object impl) throws ProcessorException {
		Cond el = (Cond) impl;
		
		If ifCond = el.getIf();
		IfProcessor ifProcessor = new IfProcessor();
		if(ifProcessor.processCondition(context, process, rule, ifCond)) {
			LOGGER.debug("Condition: matching if");
			return ifProcessor.process(context, process, rule, ifCond);
		}
		
		List<Elseif> elseConds = el.getElseif();
		if(elseConds != null) {
			ElseifProcessor elseifProcessor = new ElseifProcessor();
			for(Elseif elseifCond : elseConds) {
				if(elseifProcessor.processCondition(context, process, rule, elseifCond)) {
					LOGGER.debug("Condition: matching elseif " + elseifCond);
					return elseifProcessor.process(context, process, rule, elseifCond);
				}
			}
		}
		
		Else elseCond = el.getElse();
		if(elseCond != null) {
			LOGGER.debug("Condition: matching else");
			ElseProcessor elseProcessor = new ElseProcessor();
			return elseProcessor.process(context, process, rule, elseCond);
		}	
		
		LOGGER.debug("Condition: no matching conditions");
		
		return null;
	}
}
