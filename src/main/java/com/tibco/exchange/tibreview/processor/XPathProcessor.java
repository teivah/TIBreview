package com.tibco.exchange.tibreview.processor;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import com.tibco.exchange.tibreview.common.NamespaceContextMap;
import com.tibco.exchange.tibreview.exception.XPathProcessorException;

public class XPathProcessor {
	private final XPath xpath;

	public static final String[] TIBCO_NAMESPACES = { "bpws",
			"http://docs.oasis-open.org/wsbpel/2.0/process/executable", "tibex",
			"http://www.tibco.com/bpel/2007/extensions" };

	public XPathProcessor() {
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
}
