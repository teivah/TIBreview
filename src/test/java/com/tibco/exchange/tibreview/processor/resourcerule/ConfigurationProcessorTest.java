package com.tibco.exchange.tibreview.processor.resourcerule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
import com.tibco.exchange.tibreview.model.rules.Resourcerule;

public class ConfigurationProcessorTest {
	private static final Logger LOGGER = Logger.getLogger(ConfigurationProcessorTest.class);

	@Test
	public void testProcess() {
		TIBResource fileresource;
		try {

			fileresource = new TIBResource("src/test/resources/FileResources/ThreadPoolResource.threadPoolResource");
			fileresource.toString();
			LOGGER.info(fileresource.toString());
			assertTrue(fileresource.toString().equals("TIBResource [filePath=src/test/resources/FileResources/ThreadPoolResource.threadPoolResource, type=tp:ThreadPoolConfiguration]"));
			Configuration configuration = new Configuration();
			configuration.setType("tp:ThreadPoolConfiguration");
			Property propiedad = new Property();
			propiedad.setName("coreThreadPoolSize");
			propiedad.setCheck("configurable");
			configuration.getProperty().add(propiedad);
			ConfigurationProcessor a = new ConfigurationProcessor();
			Resourcerule rule = new Resourcerule();
			rule.setConfiguration(configuration);
			rule.setPrority(1);
			rule.setName("ConfigurationProcessorTest");
			rule.setDescription("ConfigurationProcessorTest Desc");
			List<Violation> b = a.process(new Context(), fileresource, rule, configuration);
			
			LOGGER.info("numero de errores:"+b.size());
			assertEquals(1, b.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info("error testProcess ");
			
		}
		
	}
	
	
	@Test
	public void testProcessNameSpace() {
		InputSource ns1xml = new InputSource("src/test/resources/FileResources/ThreadPoolResource.threadPoolResource");


	    NamespaceContext context = new NamespaceContextMap(
	    		"xmi", "http://www.omg.org/XMI",
	    		"xsi", "http://www.w3.org/2001/XMLSchema-instance" ,
	    		"jndi", "http://xsd.tns.tibco.com/amf/models/sharedresource/jndi" );

	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    xpath.setNamespaceContext(context);
	    XPathExpression expression;
		try {
			String eval2 = Util.xpathEval("src/test/resources/FileResources/ThreadPoolResource.threadPoolResource", Constants.RESOURCES_NAMESPACES, 
					"boolean(//jndi:configuration[ @xsi:type = 'tp:ThreadPoolConfiguration']/substitutionBindings[@template = 'coreThreadPolSize' and @propName != ''])");
			
			LOGGER.info("eval2"+eval2);
			
			expression = xpath.compile("//jndi:configuration[ @xsi:type = 'tp:ThreadPoolConfiguration']/@keepAliveTimeUnit");

		    LOGGER.info(expression.evaluate(ns1xml));
		    
		    expression = xpath.compile(" boolean(//jndi:configuration[ @xsi:type = 'tp:ThreadPoolConfiguration']/substitutionBindings[@template = 'coreThreadPolSize' and @propName != ''])");
		    
		    
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info("error testProcessNameSpace ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
