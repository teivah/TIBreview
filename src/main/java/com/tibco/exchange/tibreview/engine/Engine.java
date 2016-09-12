package com.tibco.exchange.tibreview.engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import com.tibco.exchange.tibreview.view.TIBProcess;

public class Engine {
	private final Tibrules tibrules;
	private final List<String> processes;
	private String inputType;
	private String outputType;
	private String sourcePath;
	private String targetPath;
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Engine.class);
	private static final String PROCESS_EXTENSION = ".bwp";
	private static final String PATH_PROCESSES = "/Processes";
	public static final String INPUT_PROJECT = "project";
	public static final String INPUT_PROCESS = "process";
	public static final String OUTPUT_CSV = "csv";
	public static final String OUTPUT_PMD = "pmd";
	
	public Engine(String rulePath, String configPath, String inputType, String sourcePath, String outputType, String targetPath) throws ParsingException, EngineException {
		//Parse rule
		this.tibrules = RulesParser.parseFile(rulePath);
		
		//Parse config
		//TODO
		
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
	
	public void process() {
		processProcesses();
	}
	
	private void processProcesses() {
		for(String process : processes) {
			TIBProcess tibProcess = new TIBProcess(process);
			processProcess(tibProcess);
		}
	}
	
	private void processProcess(TIBProcess tibProcess) {
		List<Rule> rules = tibrules.getProcess().getRule();
		
		for(Rule rule : rules) {
			Impl impl = rule.getImpl();
			ImplProcessor processor = new ImplProcessor();
			System.out.println(processor.process(tibProcess, impl));
		}
	}
}
