package com.tibco.exchange.tibreview.processor.processrule.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.common.TIBProcessManager;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Rule;
import com.tibco.exchange.tibreview.model.sax.PartnerLinkModel;

public class PRCycleCheck extends PRJava {
	private static final Logger LOGGER = Logger.getLogger(PRJava.class);
	
	@Override
	public List<Violation> process(Context context, List<TIBProcess> processes, Rule rule, Object impl)
			throws ProcessorException {		
		//context.getStack().push("Test");
		
		List<Violation> violations = new ArrayList<>();
		for(TIBProcess process : processes) {
			if(checkCycle(process, new HashMap<>())) {
				violations.add(Util.formatViolation(rule));
				context.getFilenames().addLast(process.getFilePath());
			}
		}
		
		return violations.size() == 0 ? null : violations;
	}
	
	private boolean checkCycle(TIBProcess process, Map<String, Void> run) {
		if(run.containsKey(process.getFullProcessName())) {
			LOGGER.debug("Cycle found: " + process.getFullProcessName());
			return true;
		}
		
		run.put(process.getFullProcessName(), null);
		
		List<PartnerLinkModel> partners = process.getStaticProcessReferencePartners();
		
		if(partners == null) {
			return false;
		}
		
		for(PartnerLinkModel partner : partners) {
			//Retrieve TIBProcess instance
			TIBProcess sub = TIBProcessManager.getInstance().getProcess(partner.getProcessName());
			
			if(sub == null) {
				LOGGER.warn("Process " + partner.getProcessName() + " not found in the process manager");
			} else {
				boolean cycle = checkCycle(sub, new HashMap<>(run));
				if(cycle) {
					return true;
				}
			}
		}
		
		return false;
	}
}
