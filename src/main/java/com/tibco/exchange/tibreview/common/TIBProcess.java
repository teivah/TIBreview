package com.tibco.exchange.tibreview.common;

public class TIBProcess {
	private String filePath;

	public TIBProcess(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "TIBProcess [filePath=" + filePath + "]";
	}
}
