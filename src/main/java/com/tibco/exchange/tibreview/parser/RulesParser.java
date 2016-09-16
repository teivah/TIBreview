package com.tibco.exchange.tibreview.parser;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.rules.Tibrules;

public class RulesParser {
	private static RulesParser instance = null;
	private static final String SCHEMA_LOCATION = "src/main/resources/schemas/tibrules.xsd";
	
	private RulesParser() {
		
	}
	
	public static RulesParser getInstance() {
		if(instance == null) {
			instance = new RulesParser();
		}
		
		return instance;
	}

	public Tibrules parseFile(String file) throws ParsingException {
		File f = new File(file);

		if (!f.exists()) {
			throw new IllegalArgumentException("File " + file + " does not exist");
		}

		FileInputStream is = null;
		try {
			is = new FileInputStream(f);
			Source source = new StreamSource(is);
			
			SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(SCHEMA_LOCATION));
			JAXBContext jc = JAXBContext.newInstance("com.tibco.exchange.tibreview.model.rules");
			
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);

			JAXBElement<Tibrules> root = unmarshaller.unmarshal(source, Tibrules.class);
			
			return root.getValue();
		} catch (Exception e) {
			throw new ParsingException("Unable to parse file " + file, e);
		}
	}
}
