package com.tibco.exchange.tibreview.processor;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import com.tibco.exchange.tibreview.common.NamespaceContextMap;
import com.tibco.exchange.tibreview.exception.XPathProcessorException;
import com.tibco.exchange.tibreview.view.TIBProcess;

public final class XPathProcessor implements Processable {
	private final XPath xpath;

	private static final Logger LOGGER = Logger.getLogger(Processable.class);
	public static final String[] TIBCO_NAMESPACES = { "bpws",
			"http://docs.oasis-open.org/wsbpel/2.0/process/executable", "tibex",
			"http://www.tibco.com/bpel/2007/extensions" };
	private static XPathProcessor INSTANCE;

	public static XPathProcessor getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new XPathProcessor();
		}
		
		return INSTANCE;
	}
	
	private XPathProcessor() {
		NamespaceContext context = new NamespaceContextMap(TIBCO_NAMESPACES);
		XPathFactory factory = XPathFactory.newInstance();
		this.xpath = factory.newXPath();
		this.xpath.setNamespaceContext(context);
	}

	public String eval(InputSource is, String xpath) throws XPathProcessorException {
		try {
			XPathExpression expression = this.xpath.compile(xpath);
			return (String) expression.evaluate(is);
		} catch(Exception e) {
			throw new XPathProcessorException("Unable to evaluate query [" + xpath + "]");
		}
	}

	@Override
	public boolean process(TIBProcess process, Object impl) {
		String el = (String) impl;
		
		try {
			//TODO Better management of the InputSource object without having to open it each time
			InputSource is = new InputSource(process.getFile());
			return Boolean.valueOf(eval(is, el));
		} catch(Exception e) {
			LOGGER.error("Query evaluation error on the file " + process.getFile(), e);
			return true;
		}
	}
}
