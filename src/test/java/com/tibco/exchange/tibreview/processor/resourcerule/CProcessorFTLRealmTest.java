package com.tibco.exchange.tibreview.processor.resourcerule;

import static org.junit.Assert.*;

import java.util.List;
import java.util.logging.Level;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.xml.sax.InputSource;

import com.tibco.exchange.tibreview.common.Constants;
import com.tibco.exchange.tibreview.common.NamespaceContextMap;
import com.tibco.exchange.tibreview.common.TIBResource;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Configuration;
import com.tibco.exchange.tibreview.model.rules.Property;
import com.tibco.exchange.tibreview.model.rules.Resource;
import com.tibco.exchange.tibreview.model.rules.Resourcerule;
import com.tibco.exchange.tibreview.model.rules.Tibrules;
import com.tibco.exchange.tibreview.parser.RulesParser;

public class CProcessorFTLRealmTest {
	private static final Logger LOGGER = Logger.getLogger(CProcessorFTLRealmTest.class);

	@Test
	public void testCProcessorFTLRealmTest() {
		TIBResource fileresource;
		try {

			fileresource = new TIBResource("src/test/resources/FileResources/FTLRealmServerConnection.ftlResource");
			fileresource.toString();
			System.out.println(fileresource.toString());
			assertTrue(fileresource.toString().equals("TIBResource [filePath=src/test/resources/FileResources/FTLRealmServerConnection.ftlResource, type=ftlsr:FTLRealmServerConnection]"));
			Resourcerule rule = new Resourcerule();
			
			Tibrules tibrules= RulesParser.getInstance().parseFile("src/test/resources/FileResources/xml/FTLRealmServerConnection.xml");
			Resource resource = tibrules.getResource();
			System.out.println(resource.getRule().size());
			assertEquals(resource.getRule().size(),1);
			ConfigurationProcessor a = new ConfigurationProcessor();
			Configuration Configuracion = resource.getRule().get(0).getConfiguration();
			
			List<Violation> b = a.process(new Context(), fileresource, resource.getRule().get(0), Configuracion);
			assertEquals(6, b.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error testProcess ");
			
		}
		
	}



}
