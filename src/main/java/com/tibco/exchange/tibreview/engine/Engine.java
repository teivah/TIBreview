package com.tibco.exchange.tibreview.engine;

import java.io.File;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.Tibrules;
import com.tibco.exchange.tibreview.model.parser.RulesParser;

public class Engine {
	private static final Logger LOGGER = Logger.getLogger(Engine.class);
	private final Tibrules rules;
	private final File project;
	
	public Engine(String projectLocation, String ruleLocation) throws ParsingException {
		project = new File(projectLocation);
		if(!project.isDirectory()) {
			throw new IllegalArgumentException(projectLocation + " is not a directory");
		}
		
		rules = RulesParser.parseFile(ruleLocation);
	}
}
