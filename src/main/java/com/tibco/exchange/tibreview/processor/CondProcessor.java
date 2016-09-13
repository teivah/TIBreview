package com.tibco.exchange.tibreview.processor;

import java.util.List;

import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.rules.Cond;
import com.tibco.exchange.tibreview.model.rules.Else;
import com.tibco.exchange.tibreview.model.rules.Elseif;
import com.tibco.exchange.tibreview.model.rules.If;
import com.tibco.exchange.tibreview.view.TIBProcess;

public final class CondProcessor implements Processable {
	@Override
	public boolean process(Context context, TIBProcess process, Object impl) throws ProcessorException {
		Cond el = (Cond) impl;
		
		If ifCond = el.getIf();
		IfProcessor ifProcessor = new IfProcessor();
		if(ifProcessor.processCondition(context, process, ifCond)) {
			return ifProcessor.process(context, process, ifCond);
		}
		
		List<Elseif> elseConds = el.getElseif();
		if(elseConds != null) {
			ElseifProcessor elseifProcessor = new ElseifProcessor();
			for(Elseif elseifCond : elseConds) {
				if(elseifProcessor.processCondition(context, process, elseifCond)) {
					return elseifProcessor.process(context, process, elseifCond);
				}
			}
		}
		
		Else elseCond = el.getElse();
		if(elseCond != null) {
			ElseProcessor elseProcessor = new ElseProcessor();
			return elseProcessor.process(context, process, elseCond);
		}	
		
		return true;
	}
}
