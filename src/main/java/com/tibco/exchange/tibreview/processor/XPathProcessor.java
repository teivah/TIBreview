package com.tibco.exchange.tibreview.processor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tibco.exchange.tibreview.common.ContextReplaceable;
import com.tibco.exchange.tibreview.common.NamespaceContextMap;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.Xpath;
import com.tibco.exchange.tibreview.view.TIBProcess;

public final class XPathProcessor implements Processable {
	private final XPath xpath;

	private static final Logger LOGGER = Logger.getLogger(Processable.class);
	public static final String[] TIBCO_NAMESPACES = { "bpws",
			"http://docs.oasis-open.org/wsbpel/2.0/process/executable", "tibex",
			"http://www.tibco.com/bpel/2007/extensions", "bwext", "http://tns.tibco.com/bw/model/core/bwext", "xsl",
			"http://www.w3.org/1999/XSL/Transform" };
	private static XPathProcessor INSTANCE = null;
	private static String DELIMITER_PROPERTY = "%";
	private static String DELIMITER_FUNCTION = "*";
	private static String TYPE_NONE = "none";

	public static XPathProcessor getInstance() {
		if (INSTANCE == null) {
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

	public boolean eval(InputSource is, String xpath) throws ProcessorException {
		try {
			XPathExpression expression = this.xpath.compile(xpath);
			String eval = (String) expression.evaluate(is);
			LOGGER.debug("Eval: " + eval);
			return Boolean.valueOf(eval);
		} catch (Exception e) {
			throw new ProcessorException("Unable to evaluate query: " + xpath);
		}
	}

	public List<Boolean> evalList(InputSource is, String xpath) throws ProcessorException {
		// TODO return type ?
		try {
			XPathExpression expression = this.xpath.compile(xpath);
			NodeList nodeList = (NodeList) expression.evaluate(is, XPathConstants.NODESET);

			if (nodeList == null || nodeList.getLength() == 0) {
				return null;
			}

			List<Boolean> list = new ArrayList<>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				// TODO add to a list
			}

			return list;
		} catch (Exception e) {
			throw new ProcessorException("Unable to evaluate query: " + xpath);
		}
	}

	private String cleanXPathRequest(String xpath) throws ProcessorException {
		if (xpath == null) {
			throw new ProcessorException("XPath expression is empty");
		}

		String clean = xpath.trim();

		if (xpath.length() == 0) {
			throw new ProcessorException("XPath expression is empty");
		}

		return clean;
	}

	private String replaceProperties(String xpath, Context context) {
		return Util.contextReplace(xpath, DELIMITER_PROPERTY, new ContextReplaceable() {
			@Override
			public String replaceAction(String s, Context context) {
				return context.getProperties().get(s);
			}

			@Override
			public String errorMessage(String s) {
				return "Property " + s + " not found";
			}
		}, context);
	}

	private String replaceFunctions(String xpath, Context context) {
		return Util.contextReplace(xpath, DELIMITER_FUNCTION, new ContextReplaceable() {
			@Override
			public String replaceAction(String s, Context context) {
				return context.getXpathFunctions().get(s);
			}

			@Override
			public String errorMessage(String s) {
				return "Function " + s + " not found";
			}
		}, context);
	}

	@Override
	public boolean process(Context context, TIBProcess process, Object impl) throws ProcessorException {
		// TODO return type (list of violation?)
		// TODO compensation for being able to retrieve a value helping the developer (e.g. bpws:link/@name) 

		Xpath el = (Xpath) impl;

		String xpath = el.getValue();

		try {
			xpath = replaceFunctions(replaceProperties(cleanXPathRequest(xpath), context), context);
		} catch (Exception e) {
			LOGGER.error("XPath request [" + xpath + "] handling error: " + e.getMessage());
			throw new ProcessorException(e);
		}

		LOGGER.debug("XPath request: " + xpath);

		try {
			// TODO Better management of the InputSource object
			InputSource is = new InputSource(process.getFilePath());

			if (el.getType() == null) {
				return eval(is, xpath);
			} else if (TYPE_NONE.equals(el.getType())) {
				evalList(is, xpath);
				return true;
			} else {
				LOGGER.error("XPath type " + el.getType() + "not recognized");
				throw new ProcessorException("XPath type " + el.getType() + "not recognized");
			}
		} catch (ProcessorException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Query evaluation error on the file " + process.getFilePath(), e);
			throw new ProcessorException(e);
		}
	}
}
