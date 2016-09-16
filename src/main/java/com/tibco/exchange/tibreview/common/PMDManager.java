package com.tibco.exchange.tibreview.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.pmd.File;
import com.tibco.exchange.tibreview.model.pmd.Pmd;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.parser.PMDParser;

public class PMDManager {
	private static final Logger LOGGER = Logger.getLogger(PMDManager.class);
	private Map<String, List<Violation>> current;
	
	public PMDManager() {
		this.current = new HashMap<>();
	}
	
	public void addViolations(String file, List<Violation> violations) {
		if(file == null || "".equals(file)) {
			throw new IllegalArgumentException("Cannot add a violation with an empty file name");
		}
		
		if(violations == null) {
			return;
		}
		
		List<Violation> list = current.get(file);
		
		if(list == null) {
			list = new ArrayList<>();
			list.addAll(violations);
			current.put(file, list);
		} else {
			list.addAll(violations);
		}
	}
	
	public void addViolations(LinkedList<String> filenames, List<Violation> violations) {
		if(violations == null) {
			return;
		}
		
		for(Violation violation : violations) {
			String file = null;
			if(filenames.isEmpty()) {
				file = "Global";
			} else {
				file = filenames.removeFirst();
			}
			List<Violation> list = current.get(file);
			if(list == null) {
				list = new ArrayList<>();
				list.add(violation);
				current.put(file, list);
			} else {
				list.add(violation);
			}
		}
	}
	
	public Pmd toPMD() {
		Pmd pmd = new Pmd();
		
		if(current.size() != 0) {
			List<File> files = new ArrayList<>(); 
			for (Map.Entry<String, List<Violation>> entry : current.entrySet()) {
			    String fileName = entry.getKey();
			    List<Violation> violations = entry.getValue();
			    
			    File file = new File();
			    file.setViolation(violations);
			    file.setName(fileName);
			    files.add(file);
			}
			pmd.setFile(files);
		}
		
		LOGGER.debug("PMD: " + pmd);
		
		return pmd;
	}
	
	public void generatePMDFile(String file) throws ParsingException {
		PMDParser.getInstance().generatePMDFile(toPMD(), file);
	}
	
	public void generateCSVFile(String file) throws ParsingException {
		PMDParser.getInstance().generateCSVFile(toPMD(), file);
	}
}
