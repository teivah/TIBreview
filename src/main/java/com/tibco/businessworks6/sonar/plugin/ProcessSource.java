package com.tibco.businessworks6.sonar.plugin;

import java.io.File;
import java.nio.charset.Charset;
import com.tibco.businessworks6.sonar.plugin.XmlFile;
import com.tibco.businessworks6.sonar.plugin.SaxParser;
import com.tibco.businessworks6.sonar.plugin.Process;

/**
 * Checks and analyzes report measurements, issues and other findings in
 * WebSourceCode.
 * 
 * @author Kapil Shivarkar
 */
public class ProcessSource extends XmlSource {

	private Process process;

	public ProcessSource(File file){
		super(file);
		this.process = new Process();
		process.setProcessXmlDocument(new SaxParser().parseDocument(file, true));	
	}

	public ProcessSource(XmlFile xmlFile) {
		super(xmlFile);
		this.process = new Process();	
	}

	@Override
	public boolean parseSource(Charset charset) {
		boolean result = super.parseSource(charset);
		return result;
	}

	public void setProcessModel(Process process){
		this.process = process;
	}

	public Process getProcessModel(){
		return process;
	}
}
