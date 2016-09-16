package com.tibco.exchange.tibreview.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.sax.PartnerLinkModel;
import com.tibco.exchange.tibreview.parser.ProcessHandler;

public class TIBProcess {
	private String filePath;
	private String fullProcessName;
	private String processName;
	private String packageName;
	private List<PartnerLinkModel> partners;
	private static final String PROCESSES_PACKAGE = "Processes";
	private static final String PROCESS_EXTENSION = ".bwp";
	private static final String PACKAGE_DELIMITER = ".";
	private static final String PROCESS_PACKAGE_DELIMITER = PACKAGE_DELIMITER + PROCESSES_PACKAGE + PACKAGE_DELIMITER;

	public TIBProcess(String filePath) throws ParsingException {
		//If the path has been set manually, we format it in a standard way
		this.filePath = new File(filePath).getAbsolutePath();
		
		if("\\".equals(File.separator)) {
			this.filePath = this.filePath.replaceAll("\\\\", "/");
		}
		
		String tmp = this.filePath.replaceAll("/", PACKAGE_DELIMITER);
		tmp = tmp.substring(0, tmp.indexOf(PROCESS_EXTENSION));
		int a = tmp.indexOf(PROCESS_PACKAGE_DELIMITER);
		int b = tmp.lastIndexOf(PACKAGE_DELIMITER);
		
		this.packageName = tmp.substring(a + PROCESS_PACKAGE_DELIMITER.length(), b);
		this.processName = tmp.substring(b + 1, tmp.length());
		this.fullProcessName = this.packageName + PACKAGE_DELIMITER + this.processName; 
		
		ProcessHandler handler = Util.handleProcess(filePath);
		this.partners = handler.getListPartnerLink();
		
		TIBProcessManager.getInstance().addProcess(fullProcessName, this);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<PartnerLinkModel> getPartners() {
		return partners;
	}

	public List<PartnerLinkModel> getStaticProcessReferencePartners() {
		List<PartnerLinkModel> sub = new ArrayList<>();
		
		for(PartnerLinkModel partner : partners) {
			if("use".equals(partner.getPartnerRole()) && !partner.isDynamic()) {
				sub.add(partner);
			}
		}
		
		return sub.size() == 0 ? null : sub;
	}
	
	public void setPartners(List<PartnerLinkModel> partners) {
		this.partners = partners;
	}

	public String getFullProcessName() {
		return fullProcessName;
	}
	
	public void setFullProcessName(String fullProcessName) {
		this.fullProcessName = fullProcessName;
	}

	@Override
	public String toString() {
		return "TIBProcess [filePath=" + filePath + ", fullProcessName=" + fullProcessName + ", processName="
				+ processName + ", packageName=" + packageName + ", partners=" + partners + "]";
	}
}
