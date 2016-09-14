package com.tibco.businessworks6.sonar.plugin;

import java.util.ArrayList;
import java.util.List;

public class Operation {
	protected String name;
	protected List<Service> operationReferencedService = new ArrayList<Service>();

	public List<Service> getOperationReferencedService() {
		return operationReferencedService;
	}

	public void setOperationReferencedService(List<Service> operationReferencedService) {
		this.operationReferencedService = operationReferencedService;
	}

	public Operation(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
