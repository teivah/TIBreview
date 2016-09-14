package com.tibco.businessworks6.sonar.plugin;

import org.w3c.dom.Node;

public class EventSource extends Activity {

	public Node node;
	
	public EventSource() {
		super();
		this.type = "start";
		this.name = "Start";
	}

	public void setNode(Node node){
		this.node = node;
	}
	
	public Node getNode(){
		return node;
	}

}
