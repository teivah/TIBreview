package com.tibco.exchange.tibreview.common;

import java.util.HashMap;
import java.util.Map;

public class TIBProcessManager {
	private static TIBProcessManager instance = null;
	private Map<String, TIBProcess> processes;
	
	private TIBProcessManager() {
		processes = new HashMap<>();
	}
	
	public static TIBProcessManager getInstance() {
		if(instance == null) {
			instance = new TIBProcessManager();
		}
		
		return instance;
	}
	
	public void addProcess(String processName, TIBProcess tibProcess) {
		processes.put(processName, tibProcess);
	}
	
	public TIBProcess getProcess(String processName) {
		return processes.get(processName);
	}
}
