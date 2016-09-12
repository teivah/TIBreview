package com.tibco.exchange.tibreview.view;

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
}
