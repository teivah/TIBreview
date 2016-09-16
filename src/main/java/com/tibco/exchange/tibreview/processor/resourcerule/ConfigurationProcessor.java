package com.tibco.exchange.tibreview.processor.resourcerule;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import com.tibco.exchange.tibreview.common.Constants;
import com.tibco.exchange.tibreview.common.NamespaceContextMap;
import com.tibco.exchange.tibreview.common.TIBResource;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Configuration;
import com.tibco.exchange.tibreview.model.rules.Property;
import com.tibco.exchange.tibreview.model.rules.Resourcerule;

public class ConfigurationProcessor implements RRProcessable {

	private final XPath xpath;
	
	private static final Logger LOGGER = Logger.getLogger(RRProcessable.class);
	private static final String REQUEST_CHECK_CONFIGURABLE_PROPERTY = "boolean(//substitutionBindings[@template = '%s' and @propName != ''])";

	public ConfigurationProcessor() {
		NamespaceContext context = new NamespaceContextMap(Constants.RESOURCES_NAMESPACES);
		XPathFactory factory = XPathFactory.newInstance();
		this.xpath = factory.newXPath();
		this.xpath.setNamespaceContext(context);
	}
	
	private String eval(InputSource is, String property) throws ProcessorException {
		try {
			String request = String.format(REQUEST_CHECK_CONFIGURABLE_PROPERTY, property);
			LOGGER.debug("XPath request: " + request);
			XPathExpression expression = this.xpath.compile(request);
			String eval = (String) expression.evaluate(is);
			LOGGER.debug("Eval: " + eval);
			return eval;
		} catch (Exception e) {
			LOGGER.error("Unable to evaluate XPath query {" + xpath + "}: " + e);
			throw new ProcessorException("Unable to evaluate XPath query {" + xpath + "}", e);
		}
	}
	
	private Violation eval(Resourcerule rule, InputSource is, String property) throws ProcessorException {
		try {
			String eval = eval(is, property);
			boolean fine = Boolean.valueOf(eval);
			if(fine) {
				return null;
			} else {
				return Util.formatViolation(rule, "Property " + property + " not configurable", true);
			}
		} catch (ProcessorException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Unable to manage XPath query {" + xpath + "}: " + e);
			throw new ProcessorException("Unable to manage XPath query {" + xpath + "}: ", e);
		}
	}
	
	@Override
	public List<Violation> process(Context context, TIBResource resource, Resourcerule rule, Configuration configuration)
			throws ProcessorException {
		if(configuration.getType().equals(resource.getType())) {
			LOGGER.debug("Matching resource " + resource.getType());
			try {
				InputSource is = new InputSource(resource.getFilePath());
	
				List<Violation> violations = new ArrayList<>();
				for(Property property : configuration.getProperty()) {
					Violation violation = eval(rule, is, property.getName());
					if(violation != null) {
						violations.add(violation);
					}
				}
				
				return violations.size() == 0 ? null : violations;
			} catch (ProcessorException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.error("Query evaluation error on the file " + resource, e);
				throw new ProcessorException(e);
			}
		} else {
			return null;
		}
	}

}
