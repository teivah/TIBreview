package com.tibco.exchange.tibreview;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.Tibrules;
import com.tibco.exchange.tibreview.model.parser.RulesParser;

public class Test {
	public static void main(String[] args) throws ParsingException {
		Tibrules rules = RulesParser.parseFile("src/test/resources/tibrules.xml");
		System.out.println(rules.getProcess().getRule().get(0).getImpl().getXpath());
	}
}
