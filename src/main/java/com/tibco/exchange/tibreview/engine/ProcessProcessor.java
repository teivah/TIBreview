package com.tibco.exchange.tibreview.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.common.PMDManager;
import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.exception.EngineException;
import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Impl;
import com.tibco.exchange.tibreview.model.rules.Rule;
import com.tibco.exchange.tibreview.model.rules.Tibrules;
import com.tibco.exchange.tibreview.processor.processrule.ImplProcessor;
import com.tibco.exchange.tibreview.processor.processrule.PRProcessable;

public class ProcessProcessor implements AssetProcessable {
	private static final String PATH_PROCESSES = "/Processes";
	private static final String PROCESS_EXTENSION = ".bwp";
	private static final Logger LOGGER = Logger.getLogger(ProcessProcessor.class);
	
	@Override
	public void process(Context context, Tibrules tibrules, PMDManager manager) throws EngineException {
		//Check if type is project or specific process
		if(!Engine.INPUT_PROCESS.equals(context.getInputType()) && !Engine.INPUT_PROJECT.equals(context.getInputType())) {
			return;
		}
		
		//List processes
		final List<String> processes;
		
		if(Engine.INPUT_PROJECT.equals(context.getInputType())) {
			try {
				processes = listProcesses(context.getSource() + PATH_PROCESSES);
			} catch(IllegalArgumentException e) {
				LOGGER.warn("Directory " + PATH_PROCESSES + " does not exist, cannot analyze the processes");
				return;
			} catch (IOException e) {
				throw new EngineException("Unable to list processes", e);
			}
		} else if(Engine.INPUT_PROCESS.equals(context.getInputType())){
			processes = new ArrayList<>();
			processes.add(context.getSource());
		} else {
			return;
		}

		LOGGER.debug("processes="+processes);
		
		processProcesses(context, tibrules, manager, processes);
	}
	
	private void processProcesses(Context context, Tibrules tibrules, PMDManager manager, List<String> processes) {
		LOGGER.info("Process TIBCO processes");
		List<TIBProcess> tibProcesses = new ArrayList<>();
		for(String process : processes) {
			try {
				TIBProcess tibProcess = new TIBProcess(process);
				tibProcesses.add(tibProcess);
				processUnitProcess(context, tibrules, manager, tibProcess);
			} catch(ParsingException e) {
				LOGGER.error("Unable to parse process " + process + ": " + e);
			}
		}
		
		processGlobalProcess(context, tibrules, manager, tibProcesses);
	}
	
	private void processUnitProcess(Context context, Tibrules tibrules, PMDManager manager, TIBProcess tibProcess) {
		LOGGER.debug("Testing unit TIBCO process: " + tibProcess.getFilePath());		
		
		List<Rule> rules = tibrules.getProcess().getRule();
		
		for(Rule rule : rules) {
			if(!rule.isGlobal()) {
				try {
					if(!context.getDisabledRules().containsKey(rule.getName())) {
						LOGGER.debug("Applying rule: " + rule.getName());
						
						Impl impl = rule.getImpl();
						PRProcessable processor = new ImplProcessor();
						try {
							manager.addViolations(tibProcess.getFilePath(), processor.process(context, tibProcess, rule, impl));
						} catch (ProcessorException e) {
							LOGGER.error("Processing exception: " + e.getMessage());
						}
					} else {
						LOGGER.debug("Rule " + rule.getName() + " is disabled");
					}
				} catch(Exception e) {
					LOGGER.error("Processing exception: " + e.getMessage());
				}
			}
		}
	}
	
	private void processGlobalProcess(Context context, Tibrules tibrules, PMDManager manager, List<TIBProcess> tibProcesses) {
		LOGGER.debug("Testing global TIBCO processes");
		
		List<Rule> rules = tibrules.getProcess().getRule();
		
		for(Rule rule : rules) {
			if(rule.isGlobal()) {
				try {
					if(!context.getDisabledRules().containsKey(rule.getName())) {
						LOGGER.debug("Applying rule: " + rule.getName());
						
						Impl impl = rule.getImpl();
						ImplProcessor processor = new ImplProcessor();
						try {
							List<Violation> violations = processor.process(context, tibProcesses, rule, impl);
							manager.addViolations(context.getFilenames(), violations);
						} catch (ProcessorException e) {
							LOGGER.error("Processing exception: " + e.getMessage());
						}
					} else {
						LOGGER.debug("Rule " + rule.getName() + " is disabled");
					}
				} catch(Exception e) {
					LOGGER.error("Processing exception: " + e.getMessage());
				}
			}
		}
	}
	
	private List<String> listProcesses(String project) throws IOException {
		return Util.listFile(project, PROCESS_EXTENSION);
	}

}
