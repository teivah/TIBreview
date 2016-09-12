package com.tibco.exchange.tibreview.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.exception.EngineException;
import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.Impl;
import com.tibco.exchange.tibreview.model.Rule;
import com.tibco.exchange.tibreview.model.Tibrules;
import com.tibco.exchange.tibreview.model.Xpathfunction;
import com.tibco.exchange.tibreview.model.Xpathfunctions;
import com.tibco.exchange.tibreview.model.parser.RulesParser;
import com.tibco.exchange.tibreview.processor.ImplProcessor;
import com.tibco.exchange.tibreview.view.TIBProcess;

public class Engine {
	private final Tibrules tibrules;
	private final List<String> processes;
	private String inputType;
	private String outputType;
	private String sourcePath;
	private String targetPath;
	private final Context context;
	
	private static final Logger LOGGER = Logger.getLogger(Engine.class);
	private static final String PROCESS_EXTENSION = ".bwp";
	private static final String PATH_PROCESSES = "/Processes";
	public static final String INPUT_PROJECT = "project";
	public static final String INPUT_PROCESS = "process";
	public static final String OUTPUT_CSV = "csv";
	public static final String OUTPUT_PMD = "pmd";
	public static final String PROPERTY_DISABLE = "rules.disable";
	public static final String PROPERTY_VALUE = "property.";
	public static final String DELIMITER = ";";
	
	public Engine(String rulePath, String configPath, String inputType, String sourcePath, String outputType, String targetPath) throws ParsingException, EngineException {
		LOGGER.info("Initializing engine with rulePath=" + rulePath + ", configPath=" + configPath + ", inputType=" + inputType + ", sourcePath=" + sourcePath + ", outputType=" + outputType + ", targetPath=" + targetPath);
		
		//Parse rule
		this.tibrules = RulesParser.parseFile(rulePath);
		
		//Init context
		this.context = new Context();
		
		//Parse config
		loadPropertiesFile(configPath);
		
		//Parse xpathfunctions
		loadXPathFunctions();
		
		LOGGER.debug("Context initialized: " + context);
		
		//Input
		this.inputType = inputType;
		if(INPUT_PROJECT.equals(inputType)) {
			try {
				this.processes = listProcesses(sourcePath + PATH_PROCESSES);
			} catch (IOException e) {
				throw new EngineException("Unable to list processes", e);
			}
		} else if(INPUT_PROCESS.equals(inputType)){
			processes = new ArrayList<>();
			processes.add(sourcePath);
		} else {
			processes = null;
		}
		LOGGER.debug("processes="+processes);
		
		//Output
		this.outputType = outputType;
		
		//Source
		this.sourcePath = sourcePath;
		
		//Target
		this.targetPath = targetPath;
	}
	
	private List<String> listProcesses(String project) throws IOException {
		return Util.listFile(project, PROCESS_EXTENSION);
	}
	
	private void loadXPathFunctions() {
		Xpathfunctions xpathFunctions = tibrules.getXpathfunctions();
		
		if(xpathFunctions != null) {
			List<Xpathfunction> functions =xpathFunctions.getXpathfunction();
			
			if(functions != null) {
				for(Xpathfunction function : functions) {
					context.getXpathFunctions().put(function.getId(), function.getValue());
				}
			}
		}
	}
	
	private void loadPropertiesFile(String configPath) throws EngineException {
		Properties properties = new Properties();
		
		try(InputStream is = new FileInputStream(configPath)) {
			properties.load(is);
			Set<Object> set = properties.keySet();
			Iterator<Object> it = set.iterator();
			
			while(it.hasNext()) {
				String key = (String)it.next();
				if(key == null || key.length() == 0) {
					continue;
				}
				
				if(PROPERTY_DISABLE.equals(key)) {
					context.setDisabledRules(parseDisabledRules(properties.getProperty(PROPERTY_DISABLE)));
				} else if(key.startsWith(PROPERTY_VALUE)) {
					String subKey = key.substring(key.indexOf(PROPERTY_VALUE) + PROPERTY_VALUE.length(), key.length());
					context.getProperties().put(subKey, properties.getProperty(key));
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Config file " + configPath + " not found");
			throw new EngineException("Config file " + configPath + " not found");
		} catch (IOException e) {
			LOGGER.error("Error while parsing config file " + configPath + ": " + e.getMessage());
			throw new EngineException("Error while parsing config file " + configPath, e);
		} catch(Exception e) {
			LOGGER.error("Error while parsing config file " + configPath + ": " + e.getMessage());
			throw new EngineException("Error while parsing config file " + configPath, e);
		}
	}
	
	private Map<String, String> parseDisabledRules(String disabled) {
		Map<String, String> map = new HashMap<>();
		
		if(disabled == null || disabled.length() == 0) {
			return map;
		}
		
		String[] rules = disabled.split(DELIMITER);
		
		if(rules == null || rules.length == 0) {
			return map;
		}
		
		for(int i=0; i<rules.length; i++) {
			map.put(rules[i], "false");
		}
		
		return map;
	}
	
	public void process() {
		LOGGER.info("Engine processing start");
		processProcesses();
		LOGGER.info("Engine processing stop");
	}
	
	private void processProcesses() {
		LOGGER.info("Process TIBCO processes");
		for(String process : processes) {
			TIBProcess tibProcess = new TIBProcess(process);
			processProcess(tibProcess);
		}
	}
	
	private void processProcess(TIBProcess tibProcess) {
		LOGGER.debug("Testing TIBCO process: " + tibProcess.getFilePath());
		
		List<Rule> rules = tibrules.getProcess().getRule();
		
		for(Rule rule : rules) {
			if(!context.getDisabledRules().containsKey(rule.getName())) {
				LOGGER.debug("Applying rule: " + rule.getName());
				
				Impl impl = rule.getImpl();
				ImplProcessor processor = new ImplProcessor();
				try {
					Boolean result = processor.process(context, tibProcess, impl);
					LOGGER.debug("Result: "+result);
				} catch (ProcessorException e) {
					LOGGER.error("Processing exception: " + e.getMessage());
				}
			} else {
				LOGGER.debug("Rule " + rule.getName() + " is disabled");
			}
		}
	}
}
