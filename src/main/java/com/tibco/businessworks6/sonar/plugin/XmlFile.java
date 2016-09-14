/*
 * SonarQube XML Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tibco.businessworks6.sonar.plugin;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * Checks and analyzes report measurements, issues and other findings in
 * WebSourceCode.
 * 
 * @author Kapil Shivarkar
 */
public class XmlFile {

	private static final Logger LOGGER = Logger.getLogger(XmlFile.class);
	private static final String XML_PROLOG_START_TAG = "<?xml";

	private File file;

	/**
	 * Number of lines removed before xml prolog if present
	 */
	private int lineDeltaForIssue = 0;
	private boolean hasCharsBeforeProlog = false;

	public XmlFile(File file) {
		this.file = file;
	}

	/**
	 * Check if the xml file starts with a prolog "&lt?xml version="1.0" ?&gt"
	 * if so, check if there is any characters prefixing it.
	 */
	public void checkForCharactersBeforeProlog(Charset charset) {
		if (file == null) {
			return;
		}

		try {
			int lineNb = 1;
			Pattern firstTagPattern = Pattern.compile("<[a-zA-Z?]+");

			for (String line : Files
					.readLines(file, charset)) {
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
			LOGGER.error("Unable to analyse file " + file.getAbsolutePath() + ": " +e);
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
			String content = Files.toString(file, charset);
			File tempFile = File.createTempFile(file.getName(),".tmp");

			int index = content.indexOf(XML_PROLOG_START_TAG);
			Files.write(content.substring(index), tempFile,
					charset);

			file = tempFile;
			if (lineDelta > 1) {
				lineDeltaForIssue = lineDelta - 1;
			}

		} catch (IOException e) {
			LOGGER.error("Unable to analyse file " + file.getAbsolutePath() + ": " + e);
		}
	}

	public int getLineDelta() {
		return lineDeltaForIssue;
	}

	public File getIOFile() {
		return file;
	}

	public int getPrologLine() {
		return lineDeltaForIssue + 1;
	}

	public boolean hasCharsBeforeProlog() {
		return hasCharsBeforeProlog;
	}

}
