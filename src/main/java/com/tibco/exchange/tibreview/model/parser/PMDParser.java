package com.tibco.exchange.tibreview.model.parser;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.pmd.Pmd;

public class PMDParser {
	//private static final String SCHEMA_LOCATION = "src/main/resources/schemas/pmd.xsd";
	private static final Logger LOGGER = Logger.getLogger(PMDParser.class);

	public static void generateXml(Pmd pmd, String file) throws ParsingException {
		File f = new File(file);
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Pmd.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(pmd, f);
		} catch (JAXBException e) {
			LOGGER.error("Error while generating XML file " + file + ": " + e);
			throw new ParsingException(e);
		}
	}
}
