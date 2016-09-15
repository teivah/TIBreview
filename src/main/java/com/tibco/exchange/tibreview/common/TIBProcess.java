package com.tibco.exchange.tibreview.common;

public class TIBProcess {
	private String filePath;
	//private Process process;

	public TIBProcess(String filePath) {
		this.filePath = filePath;
		//Lazy loading
		//this.process = null;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
//	public Process getProcess() {
//		if(process == null) {
//			//process = new ProcessSource(new File(filePath)).getProcessModel();
//		}
//		return process;
//	}

	@Override
	public String toString() {
		return "TIBProcess [filePath=" + filePath + "]";
	}
}
