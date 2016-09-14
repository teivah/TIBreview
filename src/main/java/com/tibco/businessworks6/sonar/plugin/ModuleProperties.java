package com.tibco.businessworks6.sonar.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

public class ModuleProperties {
	File modulePropertiesFile ;
	private static final String XML_PROLOG_START_TAG = "<?xml";
	private boolean hasCharsBeforeProlog = false;
	private int lineDeltaForIssue = 0;
	public ModuleProperties(File file) {
		this.modulePropertiesFile = file;
	}
	
	public int getPropertiesCount(String elementName){
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		NodeList propertyList = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			checkForCharactersBeforeProlog(Charset.defaultCharset());
			Document doc = dBuilder.parse(modulePropertiesFile);
			doc.getDocumentElement().normalize();
			propertyList = doc.getElementsByTagName(elementName);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propertyList.getLength();
	}
	
	public void checkForCharactersBeforeProlog(Charset charset) {
		try {
			int lineNb = 1;
			Pattern firstTagPattern = Pattern.compile("<[a-zA-Z?]+");

			for (String line : Files
					.readLines(modulePropertiesFile, charset)) {
				Matcher m = firstTagPattern.matcher(line);
				if (m.find()) {
					int groupIndex = line.indexOf(m.group());

					if (XML_PROLOG_START_TAG.equals(m.group())
							&& (groupIndex > 0 || lineNb > 1)) {
						hasCharsBeforeProlog = true;
					}
					break;
				}
				lineNb++;
			}

			if (hasCharsBeforeProlog) {
				processCharBeforePrologInFile(charset, lineNb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a temporary file without any character before the prolog and
	 * update the following attributes in order to correctly report issues:
	 * <ul>
	 * <li>lineDeltaForIssue
	 * <li>file
	 */
	private void processCharBeforePrologInFile(Charset charset,
			int lineDelta) {
		try {
			String content = Files.toString(modulePropertiesFile, charset);
			File tempFile = File.createTempFile(modulePropertiesFile.getName(),".tmp");

			int index = content.indexOf(XML_PROLOG_START_TAG);
			Files.write(content.substring(index), tempFile,
					charset);

			modulePropertiesFile = tempFile;
			if (lineDelta > 1) {
				lineDeltaForIssue = lineDelta - 1;
			}

		} catch (IOException e) {
			System.out.println("Unable to analyse file {}"+ modulePropertiesFile.getAbsolutePath()+"  "+e);
		}
	}

}
