package com.tibco.businessworks6.sonar.plugin;

import org.w3c.dom.Element;
import com.tibco.businessworks6.sonar.plugin.XmlHelper;
public class Activity extends ProcessNode{

	public Element getConfiguration(){
		Element element = XmlHelper.firstChildElement((Element) getNode(), "config");
		return element;
	}
	
	public Element getInputBindings(){
		Element element = XmlHelper.firstChildElement((Element) getNode(), "inputBindings");
		return element;
	}
	
}
