package com.tibco.exchange.tibreview.processor;

import java.util.List;

import com.tibco.exchange.tibreview.model.Cond;
import com.tibco.exchange.tibreview.model.Else;
import com.tibco.exchange.tibreview.model.Elseif;
import com.tibco.exchange.tibreview.model.If;
import com.tibco.exchange.tibreview.view.TIBProcess;

public final class CondProcessor implements Processable {
	@Override
	public boolean process(TIBProcess process, Object impl) {
		Cond el = (Cond) impl;
		
		If ifCond = el.getIf();
		IfProcessor ifProcessor = new IfProcessor();
		if(ifProcessor.processCondition(process, ifCond)) {
			return ifProcessor.process(process, ifCond);
		}
		
		List<Elseif> elseConds = el.getElseif();
		if(elseConds != null) {
			ElseifProcessor elseifProcessor = new ElseifProcessor();
			for(Elseif elseifCond : elseConds) {
				if(elseifProcessor.processCondition(process, elseifCond)) {
					return elseifProcessor.process(process, elseifCond);
				}
			}
		}
		
		Else elseCond = el.getElse();
		if(elseCond != null) {
			ElseProcessor elseProcessor = new ElseProcessor();
			return elseProcessor.process(process, elseCond);
		}	
		
		return true;
	}
}
