package com.tibco.exchange.tibreview.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.tibco.exchange.tibreview.model.sax.PartnerLinkModel;

public class ProcessHandler extends DefaultHandler {

	private List<PartnerLinkModel> listPartnerLink;

	private Stack<String> elementStack;
	private Stack<Object> objectStack;

	public ProcessHandler() {
		listPartnerLink = new ArrayList<>();
		elementStack = new Stack<String>();
		objectStack = new Stack<Object>();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		elementStack.push(qName);

		if("bpws:partnerLink".equals(qName)) {
			PartnerLinkModel elem = new PartnerLinkModel();
			objectStack.push(elem);
			listPartnerLink.add(elem);
			
			String myRole = attributes.getValue("myRole");
			String partnerRole = attributes.getValue("partnerRole");
			String name = attributes.getValue("name");
			
			elem.setMyRole(myRole);
			elem.setPartnerRole(partnerRole);
			elem.setName(name);
		} else if("tibex:ReferenceWire".equals(qName)) {
			if("bpws:partnerLink".equals(currentElementParent())) {
				PartnerLinkModel parent = (PartnerLinkModel)objectStack.peek();
			
				boolean dynamic = Boolean.valueOf(attributes.getValue("dynamic"));
				String processName = attributes.getValue("processName");
				String serviceName = attributes.getValue("serviceName");
				
				parent.setDynamic(dynamic);
				parent.setProcessName(processName);
				parent.setServiceName(serviceName);
			}
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		elementStack.pop();

		if ("bpws:partnerLink".equals(qName)) {
			objectStack.pop();
		}
	}

//	private String currentElement() {
//		return this.elementStack.peek();
//	}

	private String currentElementParent() {
		if (this.elementStack.size() < 2) {
			return null;
		}
		return this.elementStack.get(this.elementStack.size() - 2);
	}
	
	public List<PartnerLinkModel> getListPartnerLink() {
		return listPartnerLink;
	}
}