package com.tibco.exchange.tibreview.model.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.pmd.Pmd;
import com.tibco.exchange.tibreview.model.pmd.Violation;

public class PMDParser {
	private static PMDParser instance = null;
	private static final Logger LOGGER = Logger.getLogger(PMDParser.class);
	private static final String CSV_DELIMITER = ";";

	private PMDParser() {

	}

	public static PMDParser getInstance() {
		if (instance == null) {
			instance = new PMDParser();
		}

		return instance;
	}

	public void generatePMDFile(Pmd pmd, String filename) throws ParsingException {
		File file = new File(filename);

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Pmd.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(pmd, file);
		} catch (JAXBException e) {
			LOGGER.error("Error while generating XML file " + filename + ": " + e);
			throw new ParsingException(e);
		}
	}

	public void generateCSVFile(Pmd pmd, String filename) throws ParsingException {
		File fout = new File(filename);
		FileOutputStream fos = null;
		BufferedWriter bw = null;

		try {
			fos = new FileOutputStream(fout);
			bw = new BufferedWriter(new OutputStreamWriter(fos));

			List<com.tibco.exchange.tibreview.model.pmd.File> files = pmd.getFile();
			for (com.tibco.exchange.tibreview.model.pmd.File file : files) {
				List<Violation> violations = file.getViolation();
				for (Violation violation : violations) {
					bw.write(file.getName() + CSV_DELIMITER + violation.getRule() + CSV_DELIMITER
							+ violation.getRuleset() + CSV_DELIMITER + violation.getPriority() + CSV_DELIMITER
							+ violation.getValue());
					bw.newLine();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while generating CSV file " + filename + ": " + e);
			throw new ParsingException(e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
					LOGGER.error(e);
				}
			}
			
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					LOGGER.error(e);
				}
			}
		}
	}
}
