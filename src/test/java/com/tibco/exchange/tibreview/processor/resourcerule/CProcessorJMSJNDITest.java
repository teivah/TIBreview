package com.tibco.exchange.tibreview.processor.resourcerule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.tibco.exchange.tibreview.common.TIBResource;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Configuration;
import com.tibco.exchange.tibreview.model.rules.Resource;
import com.tibco.exchange.tibreview.model.rules.Tibrules;
import com.tibco.exchange.tibreview.parser.RulesParser;

public class CProcessorJMSJNDITest {
	private static final Logger LOGGER = Logger.getLogger(CProcessorJMSJNDITest.class);

	@Test
	public void testCProcessorJMSJNDITest() {
		TIBResource fileresource;
		try {

			fileresource = new TIBResource("src/test/resources/FileResources/JMSConnectionResourceJNDI.jmsConnResource");
			fileresource.toString();
			LOGGER.info(fileresource.toString());
			assertTrue(fileresource.toString().equals("TIBResource [filePath=src/test/resources/FileResources/JMSConnectionResourceJNDI.jmsConnResource, type=jms:JMSConnectionFactory]"));
			
			Tibrules tibrules= RulesParser.getInstance().parseFile("src/test/resources/FileResources/xml/JMSConnectionResourceJNDI.xml");
			Resource resource = tibrules.getResource();
			LOGGER.info(resource.getRule().size());
			assertEquals(resource.getRule().size(),1);
			ConfigurationProcessor a = new ConfigurationProcessor();
			Configuration Configuracion = resource.getRule().get(0).getConfiguration();
			
			List<Violation> b = a.process(new Context(), fileresource, resource.getRule().get(0), Configuracion);
			if ( b == null )
				assertEquals(0,0);
			else
			assertEquals(1, b.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info("error testProcess ");
			
		}
		
	}



}
