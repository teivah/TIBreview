package com.tibco.businessworks6.sonar.plugin;

import java.util.HashMap;

public class Service {
	protected String name;
	protected String namespace;
	protected HashMap<String, Operation> operations = new HashMap<String, Operation>();
	protected String implementationProcess;
	protected String inline;
	
	public String getInline() {
		return inline;
	}

	public void setInline(String inline) {
		this.inline = inline;
	}

	public String getImplementationProcess() {
		return implementationProcess;
	}

	public void setImplementationProcess(String implementationProcess) {
		this.implementationProcess = implementationProcess;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public void setOperations(HashMap<String, Operation> operations) {
		this.operations = operations;
	}

	public HashMap<String, Operation> getOperations() {
		return this.operations;
	}
	public Service(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
