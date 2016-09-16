package com.tibco.exchange.tibreview;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.engine.Engine;
import com.tibco.exchange.tibreview.exception.EngineException;
import com.tibco.exchange.tibreview.exception.ParsingException;

public class Main {
	private static final String USAGE = "Syntax error. Usage: TIBreview.jar -r <ruleFilePath> -c <configFilePath> -i {project|process} -s <sourcePath> -o {csv|pmd} -t <targetFilePath>";
	private static final int NB_ARGS = 6;
	private static final String DELIMITER = "-";
	private static final Logger LOGGER = Logger.getLogger(Main.class);
	
	public static enum Args {
		r, c, i, s, o, t;
	}

	public static void main(String[] args) throws Exception {
		if (args == null || args.length != NB_ARGS * 2) {
			LOGGER.error(USAGE);
			throw new IllegalArgumentException();
		}

		Map<String, String> map = null;
		try {
			map = parseArgs(args);
			validateArgs(map);
		} catch (Exception e) {
			LOGGER.error(USAGE + "\n" + e.getMessage());
			throw e;
		}
		
		Engine engine;
		try {
			engine = new Engine(map.get("r"), map.get("c"), map.get("i"), map.get("s"), map.get("o"), map.get("t"));
		} catch (ParsingException e) {
			LOGGER.error(e);
			throw e;
		} catch (EngineException e) {
			LOGGER.error(e);
			throw e;
		}
		
		engine.process();
	}

	public static Map<String, String> parseArgs(String[] args) {
		Map<String, String> map = new HashMap<>();

		for (Args arg : Args.values()) {
			boolean found = false;
			for (int i = 0; i < NB_ARGS * 2; i += 2) {
				if (args[i] == null) {
					throw new IllegalArgumentException();
				}

				if (args[i].equals(DELIMITER + arg.name())) {
					map.put(arg.name(), args[i + 1]);
					found = true;
					break;
				}
			}

			if (!found) {
				throw new IllegalArgumentException();
			}
		}

		return map;
	}

	public static void validateArgs(Map<String, String> args) {
		File f;

		// Test rule file
		f = new File(args.get("r"));
		if (!f.exists()) {
			throw new IllegalArgumentException("Rule file does not exist");
		} else if(!f.isFile()) {
			throw new IllegalArgumentException("Rule file is not a file");
		}

		// Test config file
		f = new File(args.get("c"));
		if (!f.exists()) {
			throw new IllegalArgumentException("Config file does not exist");
		} else if(!f.isFile()) {
			throw new IllegalArgumentException("Config file is not a file");
		}
		
		String projectType = args.get("i");
		if(!Engine.INPUT_PROJECT.equals(projectType) && !Engine.INPUT_PROCESS.equals(projectType)) {
			throw new IllegalArgumentException("Input type not well configured");
		}
		
		f = new File(args.get("s"));
		if(Engine.INPUT_PROJECT.equals(projectType)) {
			if (!f.exists()) {
				throw new IllegalArgumentException("Source does not exist");
			} else if(!f.isDirectory()) {
				throw new IllegalArgumentException("Source is not a directory");
			}
		} else {
			if (!f.exists()) {
				throw new IllegalArgumentException("Source does not exist");
			} else if(!f.isFile()) {
				throw new IllegalArgumentException("Source is not a file");
			}
		}
		
		String outputType = args.get("o");
		if(!Engine.OUTPUT_CSV.equals(outputType) && !Engine.OUTPUT_PMD.equals(outputType)) {
			throw new IllegalArgumentException("Output type not well configured");
		}
		
		f = new File(args.get("t"));
		if (!f.exists()) {
			throw new IllegalArgumentException("Target does not exist");
		} else if(!f.isDirectory()) {
			throw new IllegalArgumentException("Target is not a directory");
		}
	}
}
