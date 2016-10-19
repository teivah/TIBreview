package com.tibco.exchange.tibreview.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.rules.Resource;
import com.tibco.exchange.tibreview.model.rules.Tibrules;

public class RulesParserTest {

	@Test
	public void testParseFile() {
		try {
			Tibrules a = RulesParser.getInstance().parseFile("src/test/resources/tibrules.xml");
			Resource resource = a.getResource();
			assertEquals(resource.getRule().size(), 26);
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}

}
