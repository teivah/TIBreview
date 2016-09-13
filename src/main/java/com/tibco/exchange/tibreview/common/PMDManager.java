package com.tibco.exchange.tibreview.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.parser.PMDParser;
import com.tibco.exchange.tibreview.model.pmd.File;
import com.tibco.exchange.tibreview.model.pmd.Pmd;
import com.tibco.exchange.tibreview.model.pmd.Violation;

public class PMDManager {
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
		
		return pmd;
	}
	
	public void generatePMDFile(String file) throws ParsingException {
		PMDParser.getInstance().generatePMDFile(toPMD(), file);
	}
	
	public void generateCSVFile(String file) throws ParsingException {
		PMDParser.getInstance().generateCSVFile(toPMD(), file);
	}
}
