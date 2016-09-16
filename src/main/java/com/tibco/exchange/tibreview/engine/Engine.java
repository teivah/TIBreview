package com.tibco.exchange.tibreview.engine;

import java.io.File;
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
import com.tibco.exchange.tibreview.model.rules.Xpathfunction;
import com.tibco.exchange.tibreview.model.rules.Xpathfunctions;
import com.tibco.exchange.tibreview.parser.RulesParser;
import com.tibco.exchange.tibreview.processor.processrule.ImplProcessor;

public class Engine {
	private final Tibrules tibrules;
	private String outputType;
	private PMDManager manager;
	private String targetPath;
	private final Context context;
	
	private static final Logger LOGGER = Logger.getLogger(Engine.class);
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
		this.tibrules = RulesParser.getInstance().parseFile(rulePath);
		
		//Init context
		this.context = new Context();
		this.context.setInputType(inputType);
		this.context.setSource(sourcePath);
		
		//Init PMDManager
		this.manager = new PMDManager();
		
		//Parse config
		loadConfigurationFile(configPath);
		
		//Parse xpathfunctions
		loadXPathFunctions();
		
		LOGGER.debug("Context initialized: " + context);
				
		//Output
		this.outputType = outputType;
		
		//Target
		this.targetPath = targetPath;
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
	
	private void loadConfigurationFile(String configPath) throws EngineException {
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
		
		//Test process
		AssetProcessable processProcessor = new ProcessProcessor();
		try {
			processProcessor.process(context, tibrules, manager);
		} catch (EngineException e) {
			LOGGER.error("Unable to process processes: " + e);
		}
		
		//Test MANIFEST
		
		//Test properties
		
		
		LOGGER.info("Engine processing stop");
		try {
			generateOutput();
		} catch (EngineException e) {
			LOGGER.error("Unable to generate output: " + e);
		}
	}
	
	private void generateOutput() throws EngineException {
		try {
			if(OUTPUT_CSV.equals(outputType)) {
				manager.generateCSVFile(targetPath + File.separator + "csv_" + Util.getCurrentTimestamp() + ".csv");
			} else if(OUTPUT_PMD.equals(outputType)) {
				manager.generatePMDFile(targetPath + File.separator + "pmd_" + Util.getCurrentTimestamp() + ".xml");
			}
		} catch(ParsingException e) {
			throw new EngineException(e);
		}
	}
}
