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
	private static final String REQUEST_CHECK_CONFIGURABLE_PROPERTY_FILTER = "boolean(//jndi:configuration[ %s ]/substitutionBindings[@template = '%s' and @propName != ''])";
	private static final String REQUEST_CHECK_CONFIGURABLE_PROPERTY = "boolean(//substitutionBindings[@template = '%s' and @propName != ''])";

	public ConfigurationProcessor() {
		NamespaceContext context = new NamespaceContextMap(Constants.RESOURCES_NAMESPACES);
		XPathFactory factory = XPathFactory.newInstance();
		this.xpath = factory.newXPath();
		this.xpath.setNamespaceContext(context);
	}
	
	
	
	private String evalFilter(InputSource is, String property,String filter) throws ProcessorException {
		try {
			String request = String.format(REQUEST_CHECK_CONFIGURABLE_PROPERTY_FILTER, filter,property);
			System.out.println("evalFilter XPath request: " + request);
			LOGGER.debug("XPath request: " + request);
			XPathExpression expression = this.xpath.compile(request);
			//String eval = (String) expression.evaluate(is);
			String eval = Util.xpathEvalInputSource(is,Constants.RESOURCES_NAMESPACES, request);
			LOGGER.debug("Eval: " + eval);
			System.out.println("Eval: " + eval);
			return eval;
		} catch (Exception e) {
			
			LOGGER.error("Unable to evaluate XPath query {" + xpath + "}: " + e);
			throw new ProcessorException("String evalFilter Unable to evaluate XPath query {" + xpath + "}", e);
		}
	}
	
	
	
	
	private String eval(InputSource is, String property) throws ProcessorException {
		try {
			
			String request = String.format(REQUEST_CHECK_CONFIGURABLE_PROPERTY, property);
			System.out.println("XPath request: " + request);
			LOGGER.debug("XPath request: " + request);
			XPathExpression expression = this.xpath.compile(request);
			String eval = (String) expression.evaluate(is);
			LOGGER.debug("Eval: " + eval);
			System.out.println("Eval: " + eval);
			return eval;
		} catch (Exception e) {
			
			LOGGER.error("Unable to evaluate XPath query {" + xpath + "}: " + e);
			throw new ProcessorException("String eval Unable to evaluate XPath query {" + xpath + "}", e);
		}
	}
	
	private Violation eval(Resourcerule rule, InputSource is, String property) throws ProcessorException {
		try {
			String eval = new String();
			String evalbrich = rule.getConfiguration().getFilter();
			System.out.println("rule.getConfiguration().getFilter()"+rule.getConfiguration().getFilter());
			if (evalbrich.equals("")  )
			{
				System.out.println("eval init");
				eval = eval(is, property);
			}
			else
			{
				System.out.println("evalFilter init");
				eval = evalFilter(is, property,rule.getConfiguration().getFilter());
			}
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
			throw new ProcessorException("Violation eval Unable to manage XPath query {" + xpath + "}: ", e);
		}
	}
	
	@Override
	public List<Violation> process(Context context, TIBResource resource, Resourcerule rule, Configuration configuration)
			throws ProcessorException {
		if(configuration.getType().equals(resource.getType())) {
			System.out.println("Matching resource " + resource.getType());
			LOGGER.debug("Matching resource " + resource.getType());
			try {
				InputSource is = new InputSource(resource.getFilePath());
	
				List<Violation> violations = new ArrayList<>();
				for(Property property : configuration.getProperty()) {
					System.out.println("Analyze : "+property.getName());
					Violation violation = eval(rule, is, property.getName());
					if(violation != null) {
						violations.add(violation);
					}
				}
				
				return violations.size() == 0 ? null : violations;
			} catch (ProcessorException e) {
				throw e;
			} catch (Exception e) {
				System.err.println("Query evaluation error on the file " + resource);
				LOGGER.error("Query evaluation error on the file " + resource, e);
				throw new ProcessorException(e);
			}
		} else {
			return null;
		}
	}

}
