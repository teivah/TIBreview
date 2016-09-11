package com.tibco.exchange.tibreview.engine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.exception.EngineException;
import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.Tibrules;
import com.tibco.exchange.tibreview.model.parser.RulesParser;
import com.tibco.exchange.tibreview.util.Util;

public class Engine {
	private final Tibrules rules;
	private final File project;
	private final List<String> processes;
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Engine.class);
	private static final String PROCESS_EXTENSION = "";
	
	public Engine(String projectLocation, String ruleLocation) throws ParsingException, EngineException {
		project = new File(projectLocation);
		if(!project.isDirectory()) {
			throw new IllegalArgumentException(projectLocation + " is not a directory");
		}
		
		rules = RulesParser.parseFile(ruleLocation);
		try {
			processes = listProcesses(project.getAbsolutePath());
		} catch (IOException e) {
			throw new EngineException("Unable to list processes", e);
		}
	}
	
	private List<String> listProcesses(String project) throws IOException {
		return Util.listFile(project, PROCESS_EXTENSION);
	}
}
