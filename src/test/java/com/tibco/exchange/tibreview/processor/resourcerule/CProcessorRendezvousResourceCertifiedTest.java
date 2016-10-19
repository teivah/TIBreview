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

public class CProcessorRendezvousResourceCertifiedTest {
	private static final Logger LOGGER = Logger.getLogger(CProcessorRendezvousResourceCertifiedTest.class);

	@Test
	public void testCProcessorRendezvousResourceCertifiedTest() {
		TIBResource fileresource;
		try {

			fileresource = new TIBResource("src/test/resources/FileResources/RendezvousResourceCertified.rvResource");
			fileresource.toString();
			LOGGER.info(fileresource.toString());
			assertTrue(fileresource.toString().equals("TIBResource [filePath=src/test/resources/FileResources/RendezvousResourceCertified.rvResource, type=rvsharedresource:RVResource]"));
			
			Tibrules tibrules= RulesParser.getInstance().parseFile("src/test/resources/FileResources/xml/RendezvousResourceCertified.xml");
			Resource resource = tibrules.getResource();
			LOGGER.info(resource.getRule().size());
			assertEquals(resource.getRule().size(),1);
			ConfigurationProcessor a = new ConfigurationProcessor();
			Configuration Configuracion = resource.getRule().get(0).getConfiguration();
			
			List<Violation> b = a.process(new Context(), fileresource, resource.getRule().get(0), Configuracion);
			assertEquals(9, b.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info("error testProcess ");
			
		}
		
	}



}
