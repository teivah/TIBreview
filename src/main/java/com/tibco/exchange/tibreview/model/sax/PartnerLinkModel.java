package com.tibco.exchange.tibreview.model.sax;

public class PartnerLinkModel {
	private String myRole;
	private String partnerRole;
	private String name;
	private boolean dynamic;
	private String processName;
	private String serviceName;

	public String getMyRole() {
		return myRole;
	}

	public void setMyRole(String myRole) {
		this.myRole = myRole;
	}

	public String getPartnerRole() {
		return partnerRole;
	}

	public void setPartnerRole(String partnerRole) {
		this.partnerRole = partnerRole;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "PartnerLinkModel [myRole=" + myRole + ", partnerRole=" + partnerRole + ", name=" + name + ", dynamic="
				+ dynamic + ", processName=" + processName + ", serviceName=" + serviceName + "]";
	}

}
