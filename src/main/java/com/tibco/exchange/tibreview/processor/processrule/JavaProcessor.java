package com.tibco.exchange.tibreview.processor.processrule;

import java.util.List;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.common.TIBProcess;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Java;
import com.tibco.exchange.tibreview.model.rules.Rule;
import com.tibco.exchange.tibreview.processor.processrule.java.PRJava;

public class JavaProcessor implements PRProcessable {
	private static final Logger LOGGER = Logger.getLogger(JavaProcessor.class);
	private static final String BASE = "com.tibco.exchange.tibreview.processor.processrule.java.";

	@Override
	public List<Violation> process(Context context, TIBProcess process, Rule rule, Object impl) throws ProcessorException {
		Java el = (Java) impl;
		try {
			@SuppressWarnings("unchecked")
			Class<PRJava> c = (Class<PRJava>)Class.forName(BASE + el.getValue());
			PRJava prJava = c.newInstance();
			return prJava.process(context, process, rule, impl);
		} catch(Exception e) {
			LOGGER.error("Java rule " + el.getValue() + " processing error: " + e);
			throw new ProcessorException("Java rule " + el.getValue() + " processing error", e);
		}
	}

}
