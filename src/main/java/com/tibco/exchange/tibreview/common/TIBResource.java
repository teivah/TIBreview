package com.tibco.exchange.tibreview.common;

public class TIBResource {
	private String filePath;
	private String type;
	
	private static final String REQUEST_GET_TYPE = "/jndi:namedResource/@type";
	
	public TIBResource(String filePath) throws Exception {
		this.filePath = filePath;
		this.type = Util.xpathEval(filePath, Constants.RESOURCES_NAMESPACES, REQUEST_GET_TYPE);
	}

	public String getFilePath() {
		return filePath;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "TIBResource [filePath=" + filePath + ", type=" + type + "]";
	}
}
