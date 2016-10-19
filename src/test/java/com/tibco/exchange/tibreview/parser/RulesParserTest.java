package com.tibco.exchange.tibreview.parser;

import static org.junit.Assert.*;

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
			System.out.println(resource.getRule().size());
			//assertEquals(resource.getRule().size(),7);
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
