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

public class CProcessorSSLServerResourceTest {
	private static final Logger LOGGER = Logger.getLogger(CProcessorSSLServerResourceTest.class);

	@Test
	public void testCProcessorSSLServerResourceTest() {
		TIBResource fileresource;
		try {			
			fileresource = new TIBResource("src/test/resources/FileResources/SSLServerResource.sslServerResource");
			fileresource.toString();
			LOGGER.info(fileresource.toString());
			assertTrue(fileresource.toString().equals("TIBResource [filePath=src/test/resources/FileResources/SSLServerResource.sslServerResource, type=sslserver:SSLServerConfiguration]"));
			
			Tibrules tibrules= RulesParser.getInstance().parseFile("src/test/resources/FileResources/xml/SSLServerResource.xml");
			Resource resource = tibrules.getResource();
			LOGGER.info(resource.getRule().size());
			assertEquals(resource.getRule().size(),1);
			ConfigurationProcessor a = new ConfigurationProcessor();
			Configuration Configuracion = resource.getRule().get(0).getConfiguration();
			
			List<Violation> b = a.process(new Context(), fileresource, resource.getRule().get(0), Configuracion);
			assertEquals(3, b.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info("error testProcess ");
			
		}
		
	}



}
