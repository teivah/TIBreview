package com.tibco.exchange.tibreview.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.engine.Context;
import com.tibco.exchange.tibreview.exception.ParsingException;
import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Rule;
import com.tibco.exchange.tibreview.model.sax.PartnerLinkModel;
import com.tibco.exchange.tibreview.parser.ProcessHandler;

public class Util {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Util.class);

	public static List<String> listFile(final String path, final String extension) throws IOException {
		if (!new File(path).isDirectory()) {
			throw new IllegalArgumentException("Path " + path + " is not a directory");
		}

		final List<String> files = new ArrayList<>();

		Files.walk(Paths.get(path)).filter(Files::isRegularFile)
				.filter((p) -> p.toFile().getAbsolutePath().endsWith(extension)).forEach(p -> files.add(p.toString()));

		return files;
	}

	public static String mapToString(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();

		sb.append("[");

		if (map != null && map.size() > 0) {
			int i = 0;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if (i + 1 == map.size()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue());
				} else {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
				}
				i++;
			}
		}

		sb.append("]");

		return sb.toString();
	}

	public static String contextReplace(String input, String delimiter, ContextReplaceable replaceable, Context context) {
		Pattern p = Pattern.compile("\\" + delimiter + "(.*?)\\" + delimiter);
		Matcher m = p.matcher(input);
		
		while (m.find()) {
			String key = m.group(1);
			input = input.replaceAll("\\" + delimiter + key + "\\" + delimiter, replaceable.replace(key, context));
		}
		
		return input;
	}
	
	public static Violation formatViolation(Rule rule) {
		Violation violation = new Violation();
		
		violation.setPriority(rule.getPrority());
		violation.setRule(rule.getName());
		violation.setRuleset(rule.getRuleset());
		violation.setValue(rule.getDescription());
		
		return violation;
	}
	
	public static Violation formatViolation(Rule rule, String detail) {
		Violation violation = new Violation();
		
		violation.setPriority(rule.getPrority());
		violation.setRule(rule.getName());
		violation.setRuleset(rule.getRuleset());
		violation.setValue(rule.getDescription() + " Detail: " + detail);
		
		return violation;
	}
	
	public static Violation formatViolation(Rule rule, int line) {
		Violation violation = new Violation();
		
		violation.setLine(line);
		violation.setPriority(rule.getPrority());
		violation.setRule(rule.getName());
		violation.setRuleset(rule.getRuleset());
		violation.setValue(rule.getDescription());
		
		return violation;
	}
	
	public static String getCurrentTimestamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	public static ProcessHandler handleProcess(String filePath) throws ParsingException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            InputStream    xmlInput  =
                new FileInputStream(filePath);

            SAXParser      saxParser = factory.newSAXParser();
            ProcessHandler handler   = new ProcessHandler();
            saxParser.parse(xmlInput, handler);

            return handler;
        } catch (Exception e) {
            LOGGER.error("Error while handling process " + filePath + ": " + e);
            throw new ParsingException("Error while handling process " + filePath, e);
        }
	}
}
