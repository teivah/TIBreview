package com.tibco.exchange.tibreview.engine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.exception.EngineException;
import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.Impl;
import com.tibco.exchange.tibreview.model.Rule;
import com.tibco.exchange.tibreview.model.Tibrules;
import com.tibco.exchange.tibreview.model.parser.RulesParser;
import com.tibco.exchange.tibreview.processor.ImplProcessor;

public class Engine {
	private final Tibrules tibrules;
	private final String project;
	private final List<String> processes;
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Engine.class);
	private static final String PROCESS_EXTENSION = ".bwp";
	
	public Engine(String projectLocation, String ruleLocation) throws ParsingException, EngineException {
		File projectFile = new File(projectLocation);
		if(!projectFile.isDirectory()) {
			throw new IllegalArgumentException(projectLocation + " is not a directory");
		}
		this.project = projectFile.getAbsolutePath();
		
		this.tibrules = RulesParser.parseFile(ruleLocation);
		
		try {
			this.processes = listProcesses(projectFile.getAbsolutePath());
		} catch (IOException e) {
			throw new EngineException("Unable to list processes", e);
		}
	}
	
	private List<String> listProcesses(String project) throws IOException {
		return Util.listFile(project, PROCESS_EXTENSION);
	}
	
	public void processRules(String file) {
		List<Rule> rules = tibrules.getProcess().getRule();
		
		for(Rule rule : rules) {
			Impl impl = rule.getImpl();
			ImplProcessor processor = new ImplProcessor();
			System.out.println(processor.process(file, impl));
		}
	}
}
